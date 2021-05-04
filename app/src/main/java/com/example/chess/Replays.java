package com.example.chess;

import android.content.Intent;
import android.os.Bundle;

import com.example.chess.chessBoard.Replay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Replays extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Replay> replays;

    public static final String REPLAY_NAME = "replay_name";
    public static final String REPLAY_DATE = "replay_date";
    public static final String REPLAY_MOVES = "replay_moves";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replays);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Populate ListView
        // Get Replay array

        String address = "games.txt";

        try {
            FileInputStream fis = openFileInput(address);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis));

            String line;
            replays = new ArrayList<Replay>();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                replays.add(new Replay(tokens));
            }
        } catch (IOException e) {
            replays = new ArrayList<Replay>();
        }

        // Set up adapter and listView
        ArrayAdapter<Replay> adapter =
                new ArrayAdapter<>(this, R.layout.content_replays, replays);

        listView = findViewById(R.id.replays_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> showReplay(position));
    }

    private void showReplay(int pos) {
        Bundle bundle = new Bundle();

        Replay replay = (Replay)listView.getItemAtPosition(pos);

        bundle.putString(REPLAY_NAME, replay.getName());
        bundle.putString(REPLAY_DATE, replay.getDate());
        bundle.putStringArray(REPLAY_MOVES, replay.getMoves());

        Intent intent = new Intent(this, ShowReplay.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}