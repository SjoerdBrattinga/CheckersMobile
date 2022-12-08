package com.example.checkersmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "BoardActivity";

    private GameState game;
    private ImageButton[][] btnBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        newGame();
    }

    private void newGame(){
        game = new GameState();
        drawBoardTiles();
        //updateBoard()
    }

    private void drawBoardTiles(){
        int size = 8;
        btnBoard = new ImageButton[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String buttonId = "imgBtn_" + i + j;
                int btnId = getResources().getIdentifier(buttonId, "id", getPackageName());
                btnBoard[i][j] = findViewById(btnId);
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        drawLightTile(i,j);
                    } else {
                        drawDarkTile(i,j);
                    }
                }  else {
                    if (j % 2 == 0) {
                        drawDarkTile(i,j);
                    } else {
                        drawLightTile(i,j);
                    }
                }
            }
        }
    }

    private void drawLightTile(int i, int j){
        btnBoard[i][j].setBackgroundResource(R.drawable.light_tile);
    }

    private void drawDarkTile(int i, int j){
        btnBoard[i][j].setBackgroundResource(R.drawable.dark_tile);
        btnBoard[i][j].setOnClickListener(this);
        btnBoard[i][j].setTag(i + "-" + j);
    }

    @Override
    public void onClick(View view) {
        String tag = (String) view.getTag();

        int row = Integer.parseInt(tag.split("-")[0]);
        int col = Integer.parseInt(tag.split("-")[1]);
        Log.d(TAG, "onClick: row:" + row +" col:" + col);
    }
}