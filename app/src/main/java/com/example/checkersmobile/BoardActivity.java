package com.example.checkersmobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
    private TextView gameInfoTxt;
    private ImageButton[][] btnBoard;

    private Player lightPlayer, darkPlayer;
    private ArrayList<Position> highlightedTiles;
    private Position highLightedPiece;
    private GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        highlightedTiles = new ArrayList<>();

        controller = new GameController(new GameState(lightPlayer, darkPlayer), this);
        resizeBoard();
    }

    /*
     * Sends the selected position to the GameController
     */
    @Override
    public void onClick(View view) {
        String tag = (String) view.getTag();

        int row = Integer.parseInt(tag.split("-")[0]);
        int col = Integer.parseInt(tag.split("-")[1]);

        controller.handleInput(new Position(row,col));
    }

    /*
     * Draw the checkerboard tiles
     */
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
        if(!highlightedTiles.isEmpty()){
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

    public void drawMan(int row, int col, Color color){
        if(color == Color.DARK){
            btnBoard[row][col].setImageResource(R.drawable.dark_piece);
        } else if(color == Color.LIGHT){
            btnBoard[row][col].setImageResource(R.drawable.light_piece);
        }
    }

    public void drawKing(int row, int col, Color color){
        if(color == Color.DARK){
            btnBoard[row][col].setImageResource(R.drawable.dark_king);
        } else if(color == Color.LIGHT){
            btnBoard[row][col].setImageResource(R.drawable.light_king);
        }
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
        @SuppressWarnings("SuspiciousNameCombination")
        int height = size.x;

        LinearLayout layout = findViewById(R.id.board);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = width;
        params.height = height;
    }

    /*
     * The dialog menu that pops up after a game has ended
     */
    public void gameOverDialog() {

        final CharSequence choices[] = new CharSequence[]{"Play Again", "Return to Main Menu"};
        AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
        builder.setCancelable(false);
        builder.setTitle( " Wins!");
        builder.setItems(choices, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int clickValue) {

                // If user clicks New Match, create a new match
                if (clickValue == 0) {
                    //restartMatch();
                }
                // If user chooses to Return to Main Menu
                else if (clickValue == 1) {
                    //quitMatch();
                }
            }
        });
        builder.show();
    }
}