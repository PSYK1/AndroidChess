package com.example.chess;

import android.content.Intent;
import android.os.Bundle;

import com.example.chess.chessBoard.*;

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
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Replays extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> replayNames;
    private ArrayList<Replay> replays;

    public static final String REPLAY_NAME = "replay_name";
    public static final String REPLAY_DATE = "replay_date";
    public static final String REPLAY_MOVES = "replay_moves";

    private boolean lastSortDate = false;
    private boolean lastSortName = false;

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
            replayNames = new ArrayList<String>();
            replays = new ArrayList<Replay>();
            while ((line = br.readLine()) != null) {
                try {
                    Replay replay = new Replay(line.split(","));
                    replayNames.add(replay.toString());
                    replays.add(replay);
                } catch (Exception e) {
                    continue;
                }
            }
            Toast.makeText(this, "Loaded Games.txt successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("tag1", e.getMessage());
            Toast.makeText(this, "Failed to load Games.txt", Toast.LENGTH_SHORT).show();
            replayNames = new ArrayList<String>();
            replays = new ArrayList<Replay>();
        }

        // Set up adapter and listView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.replay_textview, replayNames);

        listView = (ListView) findViewById(R.id.replays_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> showReplay(position));
    }

    public void sortByDate(View v) {
        ArrayList<Replay> temp = new ArrayList<Replay>();

        while (replays.size() > 0){
            int minReplay = 0;
            for (int i = 0; i < replays.size(); i++) {
                if ((replays.get(minReplay).getDate().compareTo(replays.get(i).getDate()) < 0) == lastSortDate) {
                    minReplay = i;
                }
            }
            temp.add(replays.get(minReplay));
            replays.remove(minReplay);
        }

        replays = temp;
        replayNames = new ArrayList<String>();

        for (int i = 0; i < replays.size(); i++) {
            replayNames.add(replays.get(i).toString());
        }

        // Set up adapter and listView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.replay_textview, replayNames);

        listView = (ListView) findViewById(R.id.replays_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> showReplay(position));

        lastSortDate = !lastSortDate;

    }

    public void sortByName(View v) {
        ArrayList<Replay> temp = new ArrayList<Replay>();

        while (replays.size() > 0){
            int minReplay = 0;
            for (int i = 0; i < replays.size(); i++) {
                if ((replays.get(minReplay).getName().compareTo(replays.get(i).getName()) < 0) == lastSortName) {
                    minReplay = i;
                }
            }
            temp.add(replays.get(minReplay));
            replays.remove(minReplay);
        }

        replays = temp;
        replayNames = new ArrayList<String>();

        for (int i = 0; i < replays.size(); i++) {
            replayNames.add(replays.get(i).toString());
        }

        // Set up adapter and listView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.replay_textview, replayNames);

        listView = (ListView) findViewById(R.id.replays_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> showReplay(position));

        lastSortName = !lastSortName;
    }


    private void showReplay(int pos) {
        Bundle bundle = new Bundle();

        Replay replay = replays.get(pos);

        bundle.putString(REPLAY_NAME, replay.getName());
        bundle.putString(REPLAY_DATE, replay.getDate());
        bundle.putStringArray(REPLAY_MOVES, replay.getMoves());

        Intent intent = new Intent(this, ShowReplay.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}