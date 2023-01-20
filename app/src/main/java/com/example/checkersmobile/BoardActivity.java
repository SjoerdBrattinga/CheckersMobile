package com.example.checkersmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
//import android.widget.LinearLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "BoardActivity";

    private TextView gameInfoTxt;
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
        resizeBoard();
    }

    @Override
    public void onClick(View view) {
        String tag = (String) view.getTag();

        int row = Integer.parseInt(tag.split("-")[0]);
        int col = Integer.parseInt(tag.split("-")[1]);

        controller.handleInput(new Position(row,col));
    }

    public void drawBoard(int rows, int columns){
        btnBoard = new ImageButton[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                String buttonId = "imgBtn_" + row + col;
                int btnId = getResources().getIdentifier(buttonId, "id", getPackageName());
                btnBoard[row][col] = findViewById(btnId);

                if ((row + col) % 2 == 0) {
                    drawLightTile(row,col);
                }  else {
                    drawDarkTile(row,col);
                }
            }
        }
    }

    public void highlightTiles(ArrayList<Position> tiles, TileResource color){
        setHighlightedTiles(tiles);

        for (int i = 0; i < tiles.size(); i++) {
            btnBoard[tiles.get(i).getRow()][tiles.get(i).getCol()]
                    .setBackgroundResource(color.resourceId);
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
        btnBoard[position.getRow()][position.getCol()].setBackgroundResource(color.resourceId);
    }

    private void drawLightTile(int row, int col){
        btnBoard[row][col].setBackgroundResource(TileResource.LIGHT.resourceId);
    }

    private void drawDarkTile(int row, int col){
        btnBoard[row][col].setBackgroundResource(TileResource.DARK.resourceId);
        btnBoard[row][col].setOnClickListener(this);
        btnBoard[row][col].setTag(row + "-" + col);
    }

    public void setHighlightedTiles(ArrayList<Position> highlightedTiles) {
        this.highlightedTiles = highlightedTiles;
    }

    public void removePiece(int row, int col) {
        btnBoard[row][col].setImageResource(0);
    }

    public void drawDarkPiece(int row, int col) {
        btnBoard[row][col].setImageResource(R.drawable.dark_piece);
    }

    public void drawLightPiece(int row, int col) {
        btnBoard[row][col].setImageResource(R.drawable.light_piece);
    }

    public void drawDarkKing(int row, int col) {
        btnBoard[row][col].setImageResource(R.drawable.dark_king);
    }

    public void drawLightKing(int row, int col) {
        btnBoard[row][col].setImageResource(R.drawable.light_king);
    }

    public void setPlayerWinsText(Color player) {
        gameInfoTxt = findViewById(R.id.gameInfoTxtView);
        gameInfoTxt.setText(String.format("%s%s", player, getString(R.string.PlayerWins)));
    }

    public void setCurrentPlayerText(String currentPlayer){
        gameInfoTxt = findViewById(R.id.gameInfoTxtView);
        gameInfoTxt.setText(String.format("%s%s", currentPlayer, getString(R.string.PlayersTurn)));
    }

    private void resizeBoard(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }

        int width = size.x;
        int height = width;

        LinearLayout layout = findViewById(R.id.board);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = width;
        params.height = height;
    }
}