package com.example.chess;

import android.content.Intent;
import android.os.Bundle;

import com.example.chess.chessBoard.Replay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Replays extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> replays;

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
            replays = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                replays.add(line);
            }
            Toast.makeText(this, "Loaded Games.txt successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("tag1", e.getMessage());
            Toast.makeText(this, "Failed to load Games.txt", Toast.LENGTH_SHORT).show();
            replays = new ArrayList<String>();
        }

        // Set up adapter and listView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.replay_textview, replays);

        listView = (ListView) findViewById(R.id.replays_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> showReplay(position));
    }

    private void showReplay(int pos) {
        Bundle bundle = new Bundle();

        TextView selected = (TextView)listView.getItemAtPosition(pos);

        String data = selected.getText().toString();

        Replay replay = new Replay(data.split(","));

        bundle.putString(REPLAY_NAME, replay.getName());
        bundle.putString(REPLAY_DATE, replay.getDate());
        bundle.putStringArray(REPLAY_MOVES, replay.getMoves());

        Intent intent = new Intent(this, ShowReplay.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}