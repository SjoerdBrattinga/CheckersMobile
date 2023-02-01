package com.example.checkersmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity {
    private TextView[] highScoreTextViews;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        db = new DatabaseHelper(HighScoreActivity.this);
        loadHighScores();
    }

    private void loadHighScores(){
        ArrayList<Player> topTenPlayers = new ArrayList<>();
        Cursor cursor = db.getHighScores();

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No players found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                topTenPlayers.add(new Player(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            }
        }

        highScoreTextViews = new TextView[topTenPlayers.size()];

        for (int i = 0; i < topTenPlayers.size(); i++) {
            String txtId = "textView" + i;
            int txtViewId = getResources().getIdentifier(txtId, "id", getPackageName());
            highScoreTextViews[i] = findViewById(txtViewId);
            String rank = String.valueOf(i + 1);
            highScoreTextViews[i].setText(String.format("%s. %s - Wins: %d", rank, topTenPlayers.get(i).getName(), topTenPlayers.get(i).getGamesWon()));
        }

    }
}