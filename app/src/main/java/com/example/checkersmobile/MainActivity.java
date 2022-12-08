package com.example.checkersmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private Button playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.playBtn);
        playBtn.setOnClickListener(view -> openBoardActivity());
    }

    private void openBoardActivity() {
        Log.d(TAG, "OpenBoardActivity");
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
    }
}