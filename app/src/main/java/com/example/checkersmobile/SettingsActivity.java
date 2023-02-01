package com.example.checkersmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseHelper db;
    private EditText newPlayerName;
    private ArrayAdapter<Player> adapter;

    private ArrayList<Player> players;
    private Player lightPlayer;
    private Player darkPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DatabaseHelper(SettingsActivity.this);

        newPlayerName = findViewById(R.id.newPlayerTxtInput);

        Button addPlayerButton = findViewById(R.id.addBtn);
        addPlayerButton.setOnClickListener(view -> addNewPlayer());

        Button startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(view -> openBoardActivity());

        Spinner lightPlayerSpinner = findViewById(R.id.lightPlayerSpinner);
        Spinner darkPlayerSpinner = findViewById(R.id.darkPlayerSpinner);

        players = getPlayers();
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, players);
        //set the spinners adapter to the previously created one.

        lightPlayerSpinner.setAdapter(adapter);
        darkPlayerSpinner.setAdapter(adapter);
        lightPlayerSpinner.setOnItemSelectedListener(this);
        darkPlayerSpinner.setOnItemSelectedListener(this);
    }

    private ArrayList<Player> getPlayers(){
        ArrayList<Player> players = new ArrayList<>();

        Cursor cursor = db.getAllPlayers();

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No players found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                players.add(new Player(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            }
        }

        return  players;
    }

    private void addNewPlayer(){
        String playerName = newPlayerName.getText().toString().trim();

        for (Player player : players) {
            if(player.getName().equals(playerName)){
                Toast.makeText(SettingsActivity.this, "Name not available!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(playerName.length() > 0){
            boolean success = db.addPlayer(playerName);

            if(success){
                players = getPlayers();
                adapter.clear();
                adapter.addAll(players);
            }
        }
    }

    private void openBoardActivity() {
        if(lightPlayer == null) {
            Toast.makeText(SettingsActivity.this, "Select Light Player!", Toast.LENGTH_SHORT).show();
        } else if(darkPlayer == null){
            Toast.makeText(SettingsActivity.this, "Select Dark Player!", Toast.LENGTH_SHORT).show();
        } else if (lightPlayer.getId() == darkPlayer.getId()) {
            Toast.makeText(SettingsActivity.this, "Light & Dark Players Are The Same!", Toast.LENGTH_SHORT).show();
        } else {
            lightPlayer.setColor(Color.LIGHT);
            darkPlayer.setColor(Color.DARK);

            Intent intent = new Intent(this, BoardActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.lightPlayerSpinner){
            lightPlayer = (Player) adapterView.getItemAtPosition(i);
        } else if (adapterView.getId() == R.id.darkPlayerSpinner){
            darkPlayer = (Player) adapterView.getItemAtPosition(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}