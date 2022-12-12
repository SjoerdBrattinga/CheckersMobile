package com.example.checkersmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "BoardActivity";

    private GameState game;
    private TextView currentPlayerTxt, fromPosTxt, toPosTxt;
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
        setCurrentPlayerText();
    }

    private void setCurrentPlayerText(){
        currentPlayerTxt = findViewById(R.id.playerView);
        currentPlayerTxt.setText("Current player: " + game.getTurn().toString());
    }

    private void setToPositionText(Position selected){
        toPosTxt = findViewById(R.id.toView);
        if(selected == null){
            //selected += "Select a position";
            toPosTxt.setText("To: Select a position");
        } else {
            //toTxt += "row: " + to1.getRow() + " col: " + to1.getCol();
            toPosTxt.setText("To: row: " + selected.getRow() + " col: " + selected.getCol());
        }
    }

    private void setFromPositionText(Position selected){
        fromPosTxt = findViewById(R.id.fromView);

        if(selected == null){
            //fromTxt += "Select a position";
            fromPosTxt.setText("From: Select a position");
        } else {

            fromPosTxt.setText("From: row: " + selected.getRow() + " col: " + selected.getCol());
        }
    }

    private void drawBoard(){
        int size = game.getBoardSize();
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
//        ArrayList<Position> positions = game.getAllPiecePositions();
//        for (int i = 0; i < positions.size(); i++) {
//            drawPiece(positions.get(i));
//        }
//            Piece piece = game.getPiece(positions.get(i));
//            if(piece != null){
//                drawPiece(piece);
//            }
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                drawPiece(new Position(i,j));
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
        if (game.getPiece(position) == null) {
            btnBoard[position.getRow()][position.getCol()].setImageResource(0);
        } else if (game.getPiece(position).getColor() == Color.LIGHT){
            btnBoard[position.getRow()][position.getCol()].setImageResource(R.drawable.light_piece);

        } else if (game.getPiece(position).getColor() == Color.DARK){
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

    Position from,to;

    @Override
    public void onClick(View view) {
        String tag = (String) view.getTag();

        int row = Integer.parseInt(tag.split("-")[0]);
        int col = Integer.parseInt(tag.split("-")[1]);
        Log.d(TAG, "onClick: row:" + row +" col:" + col);
        Position selected = new Position(row,col);
        // Piece selected = game.getPiece(new Position(row,col));

        if (game.getPiece(selected) != null){
            from = selected;

            //Log.d(TAG, "onClick: from = " + row + "," + col);
        } else if (from != null ){
            if (from.equals(selected)){
                from = null;

            } else if (game.getPiece(selected) == null) {
                to = selected;

                //Log.d(TAG, "onClick: to = " + row + "," + col);
                makeMove(new Move(from,selected));
            }
        }
        setFromPositionText(from);
        setToPositionText(to);
    }
    
    private void makeMove(Move move){
        Log.d(TAG, "makeMove: ");
        if (game.isMoveLegal(move)){
            game.movePiece(move);
            updateBoard();
        } else {
            //illegal move
        }

        if (!game.playerHasMoves()){
            endTurn();
        }
    }

    private void endTurn(){
        //to = null;
        //from = null;
        game.switchTurn();
        setCurrentPlayerText();
    }
}