package com.example.checkersmobile;

import android.util.Log;

public class Piece {
    private final String TAG = "Piece";
    private Position position;
    private final Color color;
    private boolean isKing = false;

    public Piece(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public Position getPosition() {
        return position;
    }

    private void setPosition(Position position){
        this.position = position;
    }

    public void move(GameState game, Move move){
        game.setPiece(null, position);
        setPosition(move.getDestination());
        game.setPiece(this, position);
    }

    public boolean isMoveLegal(GameState game, Move move){
        if(game.getTurn() != color){
            Log.d(TAG, "isMoveLegal: Can't move other player's piece!");
            return false;
        } else if (!move.isDiagonal()) {
            Log.d(TAG, "isMoveLegal: Move must be diagonal!");
            return false;
        } else if (!isKing) {
            if (color == Color.DARK && move.isUp()){
                Log.d(TAG, "isMoveLegal: Wrong direction!");
                return false;
            } else if (color == Color.LIGHT && move.isDown()){
                Log.d(TAG, "isMoveLegal: Wrong direction!");
                return false;
            } 
        }
        if (move.distance() > 1){
            Log.d(TAG, "isMoveLegal: Illegal move distance!");
            return false;
        }
        Log.d(TAG, "isMoveLegal: Yup!");
        return true;
    }
}
