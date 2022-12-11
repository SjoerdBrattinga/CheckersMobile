package com.example.checkersmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

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
        drawBoard();
        updateBoard();
    }

    private void drawBoard(){
        int size = 8;
        btnBoard = new ImageButton[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String buttonId = "imgBtn_" + i + j;
                int btnId = getResources().getIdentifier(buttonId, "id", getPackageName());
                btnBoard[i][j] = findViewById(btnId);
                //Log.d(TAG, "drawBoard: i" + i + " j:" + j + "  % 2 = " + (i + j) % 2 );
                if ((i + j) % 2 == 0) {
                    drawLightTile(i,j);
                }  else {
                    drawDarkTile(i,j);
                }
            }
        }
    }

    private void updateBoard(){
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                Piece piece = game.getPiece(i,j);
                if(piece != null){
                    drawPiece(piece);
                }
            }
        }
    }

    private void drawPiece(Piece piece){
        if (piece.getColor() == Color.LIGHT){
            btnBoard[piece.getPosition().getRow()][piece.getPosition().getCol()]
                    .setImageResource(R.drawable.light_piece);

        } else if (piece.getColor() == Color.DARK){
            btnBoard[piece.getPosition().getRow()][piece.getPosition().getCol()]
                    .setImageResource(R.drawable.dark_piece);
        }

    }

    private void drawPiece(Position position){
        if (game.getPiece(position.getRow(),position.getCol()).getColor() == Color.LIGHT){
            btnBoard[position.getRow()][position.getCol()].setImageResource(R.drawable.light_piece);

        } else if (game.getPiece(position.getRow(),position.getCol()).getColor() == Color.DARK){
            btnBoard[position.getRow()][position.getCol()].setImageResource(R.drawable.dark_piece);
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