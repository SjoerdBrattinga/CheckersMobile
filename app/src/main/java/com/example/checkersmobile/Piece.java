package com.example.checkersmobile;

import android.util.Log;

import java.util.ArrayList;

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

        if(move.isJump()){
            game.setPiece(null, move.getInBetween());
        }
    }

    public boolean isMoveLegal(GameState game, Move move){
        if (!move.isDiagonal()) {
            Log.d(TAG, "isMoveLegal: Move must be diagonal!");
            return false;
        }
//        if (game.getPiece(move.getDestination()) != null){
//            Log.d(TAG, "isMoveLegal: Tile occupied");
//            return false;
//        }
        if (!isKing) {
            if (color == Color.DARK && move.isUp()){
                Log.d(TAG, "isMoveLegal: Wrong direction!");
                return false;
            } else if (color == Color.LIGHT && move.isDown()){
                Log.d(TAG, "isMoveLegal: Wrong direction!");
                return false;
            }
        }
        if (move.isJump()){
            if (game.getPiece(move.getInBetween()) == null){
                Log.d(TAG, "isMoveLegal: Can\'t jump empty tile!");
                return false;
            }
            if (game.getPiece(move.getInBetween()).getColor() == this.color){
                Log.d(TAG, "isMoveLegal: Can\'t capture your own piece!");
                return false;
            }
        }

        return true;
    }

//    public ArrayList<Move> getMoves(GameState game){
//        ArrayList<Move> moves;
//        int direction;
//        if(color == Color.LIGHT){
//            direction = 1;
//        } else  {
//            direction = -1;
//        }
//        if (game.isMoveLegal(new Move(position , new Position(position.getRow() + direction, position.getCol() -1)))){
//
//        }
//
//
//
//
//    }
}
