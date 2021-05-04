package com.example.chess;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chess.chessBoard.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> moves = new ArrayList<>();

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView info;
    private Button btn, btnTwo;

    private Board board = new Board();

    private Board prev = board;

    String pathSeparator = System.getProperty("file.separator");

    String turn = "w";
    String pos1 = "";
    String pos2 = "";

    private int[] loc;
    boolean resign = false;
    boolean draw = false;
    boolean checkmate = false;
    boolean drawOffer = false;

    int confirmSaveFileNum = 0;

    int selectionCount = 0;

    /**
     * Returns the piece a pawn should be promoted to, if the function is passed a string other than q,r,b, or n case does not matter
     * it will return a Queen piece and the color of the piece is determined by the turn parameter.
     *
     * @param promo the type of piece to promote to
     * @param turn  the color of the piece to promote to
     * @return the piece to promote to
     */
    public Piece getPromotion(String promo, String turn) {
        Piece p = new Queen(turn);

        if (promo.toLowerCase().equals("q")) {
            p = new Queen(turn);
            p.setMoved(true);
            return p;
        }
        if (promo.toLowerCase().equals("r")) {
            p = new Rook(turn);
            p.setMoved(true);
            return p;
        }
        if (promo.toLowerCase().equals("b")) {
            p = new Bishop(turn);
            p.setMoved(true);
            return p;
        }
        if (promo.toLowerCase().equals("n")) {
            p = new Knight(turn);
            p.setMoved(true);
            return p;
        }
        p.setMoved(true);
        return p;
    }

    /**
     * Takes in input for pos1 and pos2 in columnRow format. Returns a Board with the piece from pos1 moved to pos2 if the move is valid
     * otherwise returns an IllegalArgumentException
     *
     * @param pos1  the location of the piece needed to be moved in columnRow format
     * @param pos2  the location the piece needs to be moved to in columnRow format
     * @param promo if a pawn is to be promoted the piece to be promoted to else null if a pawn is not being promoted
     * @param turn  the color of the current player
     * @return the board after the changed position of a piece
     */
    public Board setPosition(String pos1, String pos2, Piece promo, String turn) {

        int row1 = 8 - Integer.parseInt(pos1.substring(1));
        int col1 = (pos1.toLowerCase().charAt(0)) - 97;

        int row2 = 8 - Integer.parseInt(pos2.substring(1));
        int col2 = (pos2.toLowerCase().charAt(0)) - 97;

        Piece p = board.getPiece(row1, col1);

        if (p == null) throw new IllegalArgumentException();

        if (p.move(row1, col1, row2, col2, board, turn)) {
            Piece temp = board.getPiece(row2, col2);

            // Check if this puts king in jeopardy
            // Check if castling first, since we need to check for jeopardy multiple times
            if (p instanceof King && Math.abs(col2 - col1) > 1) {
                // We must be castling.
                //First, make sure that we aren't castling through check
                int step = ((col2 - col1) / Math.abs(col2 - col1));
                for (int i = col1; i != col2 + step; i += step) {
                    board.setPiece(row1, i, p);
                    if (i != col1) board.setPiece(row1, i - step, null);
                    if (isCheck(turn)) {
                        // Can't castle through check
                        // Undo the move
                        board.setPiece(row1, col1, p);
                        board.setPiece(row2, i, null);
                        throw new IllegalArgumentException();
                    }
                }
                // If we make it here, there is no check, so we can move the rook too
                int rookCol = 0;
                if (col2 > 3) rookCol = 7;
                Piece rook = board.getPiece(row2, rookCol);
                rook.setMoved(true);
                board.setPiece(row2, col2 - step, rook);
                board.setPiece(row2, rookCol, null);

            } else {
                // Not castling

                board.setPiece(row2, col2, p);
                board.setPiece(row1, col1, null);

                // Check if the move puts your king in jeopardy
                if (isCheck(turn)) {
                    // That move put your own king in jeopardy, so it's invalid
                    // Undo the move
                    board.setPiece(row1, col1, p);
                    board.setPiece(row2, col2, temp);
                    throw new IllegalArgumentException();
                }
            }

            p.setMoved(true);

            // Check if this is en passant

            if (p instanceof Pawn) {
                if ((row2 != row1 && col2 != col1)) {

                    // get the tile passed by
                    Piece passedBy = board.getPiece(row1, col2);
                    // Check if it's a pawn
                    if (passedBy != null && (passedBy instanceof Pawn)) {
                        // kill it if it just did a double stride
                        if (!passedBy.getColor().equals(turn) && ((Pawn) passedBy).getEnPassant()) {
                            board.setPiece(row1, col2, null);
                        }
                    }
                }
            }

            if (board.getEnPassant() != null) board.getEnPassant().setEnPassant(false);
            board.setEnPassant(null);

            // Check if this pawn did a double stride
            if (p instanceof Pawn) {
                if (Math.abs(row2 - row1) == 2) {
                    // double stride
                    ((Pawn) p).setEnPassant(true);
                    board.setEnPassant(((Pawn) p));
                }
            }

        } else {
            throw new IllegalArgumentException();
        }

        if ((row2 == 7 || row2 == 0) && board.getPiece(row2, col2) instanceof Pawn) {
            board.setPiece(row2, col2, promo);
        }

        return board;
    }

    /**
     * Returns an arraylist that represents all possible moves for the player represented by the color parameter
     *
     * @param color the color of the player for which all moves are being calculated for
     * @return an arraylist for all moves of a player
     */
    public ArrayList<Move> allPossibleMoves(String color) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int x = 0; x < 8; x++) {
                if (board.getPiece(i, x) != null && board.getPiece(i, x).getColor().equals(color)) {
                    Piece p = board.getPiece(i, x);
                    for (int y = 0; y < 8; y++) {
                        for (int z = 0; z < 8; z++) {
                            if (p.move(i, x, y, z, board, color)) {
                                moves.add(new Move(i, x, y, z));
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return moves;
    }

    /**
     * Returns the location of the king with the given color
     *
     * @param color the color of the king that the location is being calculated for
     * @return the location of the king with the given color
     */
    public int[] getKingLocation(String color) {
        int[] result = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int x = 0; x < 8; x++) {
                if ((board.getPiece(i, x) != null) && board.getPiece(i, x).toString().equals(color + "k")) {
                    result[0] = i;
                    result[1] = x;
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * Takes in a color and returns a boolean value that represents if the king of that specific color is in check.
     *
     * @param color the color of the king that is being checked if it is in check
     * @return A boolean value that represents if the king with the given color is in check.
     */
    public boolean isCheck(String color) {
        int[] kingLocation = getKingLocation(color.toLowerCase());

        String opColor = (color.toLowerCase().equals("w")) ? "b" : "w";

        for (int i = 0; i < 8; i++) {
            for (int x = 0; x < 8; x++) {
                if (board.getPiece(i, x) != null && board.getPiece(i, x).getColor().equals(opColor)) {
                    if (board.getPiece(i, x).move(i, x, kingLocation[0], kingLocation[1], board, opColor)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns a boolean value representing if the king of the given color is in check mate
     *
     * @param color the color of the king that is being checked if it is in check mate
     * @return A boolean value that represents if the king with the given color is in check mate.
     */
    public boolean isCheckMate(String color) {

        if (isCheck(color)) {
            ArrayList<Move> moves = allPossibleMoves(color);

            BoardState b = new BoardState();
            b.board = board.copy();

            for (int x = 0; x < moves.size(); x++) {

                int row1 = 8 - moves.get(x).getRow1();
                char col1 = (char) (moves.get(x).getCol1() + 97);
                int row2 = 8 - moves.get(x).getRow2();
                char col2 = (char) (moves.get(x).getCol2() + 97);

                String loc1 = col1 + "" + row1;
                String loc2 = col2 + "" + row2;

                try {
                    b.setPosition(loc1, loc2, null, color);
                    if (!(b.isCheck(color))) {
                        return false;
                    } else {
                        b.board = board.copy();
                    }
                } catch (IllegalArgumentException e) {

                }
            }
        } else {
            return false;
        }
        return true;
    }

    public void draw(View v) {

        dialogBuilder = new AlertDialog.Builder(this);
        View popup = getLayoutInflater().inflate(R.layout.draw_popup, null);

        info = (TextView) popup.findViewById(R.id.info);
        btn = (Button) popup.findViewById(R.id.accept);
        btnTwo = (Button) popup.findViewById(R.id.decline);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                saveOptionOnDraw();
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        String player = (turn.equals("w")) ? "White" : "Black";

        info.setText(player + " has offered a draw");

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void resign(View v) {
        String winner = (turn.equals("w") ? "Black" : "White");
        saveOptionOnCheckmate(winner);
    }

    public void saveOptionOnDraw() {
        dialogBuilder = new AlertDialog.Builder(this);
        View popup = getLayoutInflater().inflate(R.layout.save_option_popup, null);

        info = (TextView) popup.findViewById(R.id.txt);
        btn = (Button) popup.findViewById(R.id.save);
        btnTwo = (Button) popup.findViewById(R.id.noSave);

        info.setText("Draw");

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                save();
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                endPopup("The game has not ");
            }
        });
    }

    public void saveOptionOnCheckmate(String winner) {
        dialogBuilder = new AlertDialog.Builder(this);
        View popup = getLayoutInflater().inflate(R.layout.save_option_popup, null);

        info = (TextView) popup.findViewById(R.id.txt);
        btn = (Button) popup.findViewById(R.id.save);
        btnTwo = (Button) popup.findViewById(R.id.noSave);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                save();
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                endPopup("The game has not");
            }
        });

        info.setText(winner + " has won");

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void save() {
        dialogBuilder = new AlertDialog.Builder(this);
        View popup = getLayoutInflater().inflate(R.layout.save_popup, null);

        EditText et = (EditText) popup.findViewById(R.id.gameName);

        btn = (Button) popup.findViewById(R.id.saveButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSaveFileNum = saveGame(et.getText().toString());
                dialog.dismiss();
                if(confirmSaveFileNum == 1) {
                    endPopup("The game has");
                }
                else {
                    tryAgain();
                }
            }
        });

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();

    }

    public int saveGame(String fileName) {
        try {
            FileOutputStream fos = openFileOutput("games.txt", Context.MODE_APPEND);

            fos.write((fileName + "," + LocalDateTime.now().toString()+",").getBytes());
            for (int x = 0; x < moves.size(); x++) {
                fos.write((moves.get(x)+",").getBytes());
            }
            fos.write("\n".getBytes());
            fos.close();
            Toast.makeText(getBaseContext(), "Success " + getFilesDir(), Toast.LENGTH_LONG).show();
            return 1;
        } catch (IOException e) {
            Toast.makeText(this, "File Already Exists", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    public void tryAgain() {
        dialogBuilder = new AlertDialog.Builder(this);
        View popup = getLayoutInflater().inflate(R.layout.try_again, null);

        btn = (Button) popup.findViewById(R.id.tryAgain);
        btnTwo = (Button) popup.findViewById(R.id.no);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                save();
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                endPopup("The game has not");
            }
        });

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void endPopup(String text) {
        dialogBuilder = new AlertDialog.Builder(this);
        View popup = getLayoutInflater().inflate(R.layout.end_popup, null);

        info = (TextView) popup.findViewById(R.id.report);
        btn = (Button) popup.findViewById(R.id.newGame);

        info.setText(text + " been saved");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView trn = (TextView) findViewById(R.id.trn);
                trn.setText("White's move");
                dialog.dismiss();
                board = new Board();
                turn = "w";
                moves = new ArrayList<String>();
                setListeners();
                printBoard();
            }
        });

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void clearBoard() {
        TableLayout t = findViewById(R.id.table);
        for (int x = 0; x < t.getChildCount(); x++) {
            TableRow row = (TableRow) t.getChildAt(x);
            for (int i = 0; i < row.getChildCount(); i++) {
                ConstraintLayout c = (ConstraintLayout) row.getChildAt(i);
                c.removeAllViews();
            }
        }
    }

    public void printBoard() {
        clearBoard();
        TableLayout t = findViewById(R.id.table);
        for (int x = 0; x < t.getChildCount(); x++) {
            TableRow row = (TableRow) t.getChildAt(x);
            for (int i = 0; i < row.getChildCount(); i++) {
                Piece p = board.getPiece(x, i);

                if (p != null) {
                    ConstraintLayout c = (ConstraintLayout) row.getChildAt(i);
                    ImageView img = new ImageView(this);
                    int resourceImg = getResources().getIdentifier(p.toString(), "drawable", getPackageName());
                    img.setImageResource(resourceImg);
                    c.addView(img);
                }
            }
        }
    }

    public void disallowTouch(ViewParent parent, boolean isDisallow) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(isDisallow);
        }
    }

    public boolean equal(Board a, Board b) {
        for(int x = 0; x < 8; x++) {
            for(int i = 0; i < 8; i++) {
                if(a.getPiece(x,i) == null || b.getPiece(x,i) == null) {
                    if(a.getPiece(x,i) == null && b.getPiece(x,i) == null) {

                    } else {
                        return false;
                    }
                }
                else {
                    if (a.getPiece(x, i).getName().equals(b.getPiece(x, i).getName())) {

                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void undo(View v) {
        if(!equal(board,prev)) {
            board = prev;
            printBoard();
            turn = (turn=="w")?"b":"w";
            TextView trn = findViewById(R.id.trn);
            trn.setText((turn=="w")?"White's move":"Black's Move");
        }
        else {
            Toast.makeText(this, "Can not undo", Toast.LENGTH_SHORT).show();
        }
    }

    public void setListeners() {
        TextView trn = (TextView) findViewById(R.id.trn);
        TableLayout t = findViewById(R.id.table);
        for (int x = 0; x < t.getChildCount(); x++) {
            TableRow row = (TableRow) t.getChildAt(x);
            for (int i = 0; i < row.getChildCount(); i++) {
                ConstraintLayout c = (ConstraintLayout) row.getChildAt(i);
                c.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ViewParent parent = v.getParent();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN: {
                                disallowTouch(parent, true);
                                if (selectionCount == 0) {
                                    pos1 = v.getResources().getResourceEntryName(v.getId());
                                    selectionCount++;
                                } else {
                                    int row1 = 8 - Integer.parseInt(pos1.substring(1));
                                    int col1 = (pos1.toLowerCase().charAt(0)) - 97;

                                    pos2 = v.getResources().getResourceEntryName(v.getId());
                                    try {
                                        BoardState b = new BoardState();
                                        b.board = board.copy();
                                        try {
                                            b.setPosition(pos1, pos2, new Queen(turn), turn);
                                            prev = board.copy();
                                        } catch(IllegalArgumentException e) {

                                        }
                                        setPosition(pos1, pos2, new Queen(turn), turn);
                                        moves.add(pos1 + " " + pos2);

                                        if ((turn.equals("w") && isCheck("b"))) {
                                            if (isCheckMate("b")) {
                                                saveOptionOnCheckmate("White");
                                            } else {
                                                Toast.makeText(MainActivity.this, "Black is in check", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        if ((turn.equals("b") && isCheck("w"))) {
                                            if (isCheckMate("w")) {
                                                saveOptionOnCheckmate("Black");
                                            } else {
                                                Toast.makeText(MainActivity.this, "White is in check", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        turn = (turn.equals("w")) ? "b" : "w";

                                        if (turn.equals("w")) {
                                            trn.setText("White's move");
                                        } else {
                                            trn.setText("Black's move");
                                        }
                                    } catch (IllegalArgumentException e) {
                                        Toast.makeText(MainActivity.this, "Illegal Move", Toast.LENGTH_SHORT).show();
                                    }
                                    printBoard();
                                    selectionCount = 0;
                                }

                                return false;
                            }
                            default:
                                disallowTouch(parent, true);
                                return false;
                        }
                    }
                });
            }
        }

    }

    public int aiMoveHelper(Move m) {
        TextView trn = (TextView) findViewById(R.id.trn);
        try {
            int row1 = 8 - m.getRow1();
            char col1 = (char) (m.getCol1() + 97);
            int row2 = 8 - m.getRow2();
            char col2 = (char) (m.getCol2() + 97);

            String loc1 = col1 + "" + row1;
            String loc2 = col2 + "" + row2;

            Piece p = new Queen(turn);

            prev = board.copy();

            setPosition(loc1, loc2, p, turn);

            moves.add(loc1 + " " + loc2);

            if ((turn.equals("w") && isCheck("b"))) {
                if (isCheckMate("b")) {
                    saveOptionOnCheckmate("White");
                } else {
                    Toast.makeText(this, "Black is in check", Toast.LENGTH_SHORT).show();
                }
            }

            if ((turn.equals("b") && isCheck("w"))) {
                if (isCheckMate("w")) {
                    saveOptionOnCheckmate("Black");
                } else {
                    Toast.makeText(this, "White is in check", Toast.LENGTH_SHORT).show();
                }
            }

            turn = turn.equals("w")?"b":"w";
            trn.setText((turn.equals("w")?"White's move":"Black's move"));
            printBoard();
        }
        catch(IllegalArgumentException e) {
            return 0;
        }
        return 1;
    }

    public void aiMove(View v) {
        ArrayList<Move> moves = allPossibleMoves(turn);

        int num = 0;
        int x = 0;
        while(num == 0) {
            x = (int) (Math.random() * ((moves.size() - 1)));
            num = aiMoveHelper(moves.get(x));
        }
    }

    public void switchToReplays(View v) {
        Intent intent = new Intent(this, Replays.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_board);
        TextView trn = (TextView) findViewById(R.id.trn);
        trn.setText("White's move");
        setListeners();
        printBoard();
    }
}