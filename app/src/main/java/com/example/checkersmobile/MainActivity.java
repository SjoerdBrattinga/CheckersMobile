package com.example.checkersmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playBtn = findViewById(R.id.startBtn);
        Button highScoreBtn = findViewById(R.id.highScoreBtn);
        playBtn.setOnClickListener(view -> openSettingsActivity());
        highScoreBtn.setOnClickListener(view -> openHighScoreActivity());
    }

    private void openBoardActivity() {
        Log.d(TAG, "OpenBoardActivity");

        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
    }

    private void openHighScoreActivity() {
        Log.d(TAG, "OpenHighScoreActivity");

        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Log.d(TAG, "OpenSettingsActivity");

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}