package com.example.checkersmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "BoardActivity";

    private TextView currentPlayerTxt, fromPosTxt, toPosTxt;
    private ImageButton[][] btnBoard;
    private ArrayList<Position> highlightedTiles;
    private Position highLightedPiece;
    private GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        highlightedTiles = new ArrayList<>();
        controller = new GameController(new GameState(), this);
    }

    public void drawBoard(int rows, int columns){
        btnBoard = new ImageButton[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String buttonId = "imgBtn_" + i + j;
                int btnId = getResources().getIdentifier(buttonId, "id", getPackageName());
                btnBoard[i][j] = findViewById(btnId);
                if ((i + j) % 2 == 0) {
                    drawLightTile(i,j);
                }  else {
                    drawDarkTile(i,j);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        String tag = (String) view.getTag();

        int row = Integer.parseInt(tag.split("-")[0]);
        int col = Integer.parseInt(tag.split("-")[1]);

        controller.handleInput(new Position(row,col));
    }

    public void setCurrentPlayerText(String player){
        currentPlayerTxt = findViewById(R.id.playerView);
        currentPlayerTxt.setText(String.format("%s%s", player, getString(R.string.PlayersTurn)));
    }

    public void highlightTiles(ArrayList<Position> tiles, TileResource color){
        setHighlightedTiles(tiles);
        for (int i = 0; i < tiles.size(); i++) {
            btnBoard[tiles.get(i).getRow()][tiles.get(i).getCol()]
                    .setBackgroundResource(color.drawableId);
        }
    }

    public void resetTileHighlights(){
        if(highlightedTiles.size() > 0){
            highlightTiles(highlightedTiles, TileResource.DARK);
            highlightedTiles.clear();
        }
        if(highLightedPiece != null){
            highLightTile(highLightedPiece, TileResource.DARK);
            highLightedPiece = null;
        }
    }

    public void highLightTile(Position position, TileResource color){
        highLightedPiece = position;
        btnBoard[position.getRow()][position.getCol()].setBackgroundResource(color.drawableId);
    }

    private void drawLightTile(int i, int j){
        btnBoard[i][j].setBackgroundResource(TileResource.LIGHT.drawableId);
    }

    private void drawDarkTile(int i, int j){
        btnBoard[i][j].setBackgroundResource(R.drawable.dark_tile);
        btnBoard[i][j].setOnClickListener(this);
        btnBoard[i][j].setTag(i + "-" + j);
    }

    public void setHighlightedTiles(ArrayList<Position> highlightedTiles) {
        this.highlightedTiles = highlightedTiles;
    }

    public void removePiece(int i, int j) {
        btnBoard[i][j].setImageResource(0);
    }

    public void drawDarkPiece(int i, int j) {
        btnBoard[i][j].setImageResource(R.drawable.dark_piece);
    }

    public void drawLightPiece(int i, int j) {
        btnBoard[i][j].setImageResource(R.drawable.light_piece);
    }

    public void drawDarkKing(int i, int j) {
        btnBoard[i][j].setImageResource(R.drawable.dark_king);
    }

    public void drawLightKing(int i, int j) {
        btnBoard[i][j].setImageResource(R.drawable.light_king);
    }

    private void setToPositionText(Position selected){
        toPosTxt = findViewById(R.id.toView);
        if(selected == null){
            toPosTxt.setText("To: Select a position");
        } else {
            toPosTxt.setText("To: row: " + selected.getRow() + " col: " + selected.getCol());
        }
    }

    private void setFromPositionText(Position selected){
        fromPosTxt = findViewById(R.id.fromView);

        if(selected == null){
            fromPosTxt.setText("From: Select a position");
        } else {
            fromPosTxt.setText("From: row: " + selected.getRow() + " col: " + selected.getCol());
        }
    }
}