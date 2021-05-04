package com.example.chess;

import android.os.Bundle;

import com.example.chess.chessBoard.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowReplay extends AppCompatActivity {

    ArrayList<Board> configurations;
    int currIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_replay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get bundle data

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString(Replays.REPLAY_NAME);
        String date = bundle.getString(Replays.REPLAY_DATE);
        String[] moves = bundle.getStringArray(Replays.REPLAY_MOVES);

        // Load configurations
        configurations = new ArrayList<Board>();
        BoardState boardState = new BoardState();

        configurations.add(boardState.board.copy());

        String turn = "w";
        for (String move:moves) {
            String[] positions = move.split(" ");

            if(positions.length == 3) {
                Piece p = boardState.getPromotion(positions[2], turn);
                boardState.setPosition(positions[0], positions[1], p, turn);
            }
            else {
                Piece p = boardState.getPromotion(" ", turn);
                boardState.setPosition(positions[0], positions[1], p, turn);
            }

            configurations.add(boardState.board.copy());

            if (turn.equals("w")) turn = "b";
            else turn = "w";
        }

        displayConfiguration();

    }

    private void displayConfiguration() {
        if (currIndex >= configurations.size()) {
            return;
        }
        Board config = configurations.get(currIndex);

        clearBoard();

        TableLayout t = findViewById(R.id.r_table);
        for (int x = 0; x < t.getChildCount(); x++) {
            TableRow row = (TableRow) t.getChildAt(x);
            for (int i = 0; i < row.getChildCount(); i++) {
                Piece p = config.getPiece(x, i);

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

    public void clearBoard() {
        TableLayout t = findViewById(R.id.r_table);
        for (int x = 0; x < t.getChildCount(); x++) {
            TableRow row = (TableRow) t.getChildAt(x);
            for (int i = 0; i < row.getChildCount(); i++) {
                ConstraintLayout c = (ConstraintLayout) row.getChildAt(i);
                c.removeAllViews();
            }
        }
    }

    public void nextConfiguration(View v) {
        if (currIndex == configurations.size()-1) {
            Toast.makeText(this, "Already at final turn", Toast.LENGTH_SHORT).show();
        } else {
            currIndex++;
            displayConfiguration();
        }
    }

    public void previousConfiguration(View v) {
        if (currIndex == 0) {
            Toast.makeText(this, "Already at first turn", Toast.LENGTH_SHORT).show();
        } else {
            currIndex--;
            displayConfiguration();
        }
    }
}