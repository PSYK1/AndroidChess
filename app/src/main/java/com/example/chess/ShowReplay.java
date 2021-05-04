package com.example.chess;

import android.os.Bundle;

import com.example.chess.chessBoard.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

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

        // Display configurations



    }
}