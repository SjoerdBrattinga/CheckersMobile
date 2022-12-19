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

//    public ArrayList<Move> getPossibleMoves(GameState game){
//        ArrayList<Move> moves;
//        ArrayList<Position> destinations = new ArrayList<>();
//        int direction;
//
//        if(color == Color.LIGHT){
//            Move move = new Move(position, position.getUpLeft());
//        } else  if(color == Color.DARK){
//            direction = -1;
//        }
//    }
//    public boolean hasJumps(GameState game){
//        Position[] destinations = position.getDiagonal(2);
//        for (Position destination : destinations) {
//            Move move = new Move(position, destination);
//
//            return move.isJump()
//                    //&& game.getPiece(move.getDestination()) == null
//                    && game.getPiece(move.getInBetween()).getColor() != color
//                    && game.isMoveLegal(move);
//        }
//
//        return false;
//    }
    public boolean hasJumps(GameState game){
        Position[] destinations = position.getDiagonal(2);
        for (Position destination : destinations) {
            Move move = new Move(position, destination);

            if(game.isMoveLegal(move)
                    && move.isJump()
                    && game.getPiece(move.getInBetween()) != null
                    && game.getPiece(move.getInBetween()).getColor() != color){
                Log.d(TAG, "hasJumps: True");
                Log.d(TAG, "hasJumps: Move: " + move);

                return true;
            }
        }

        return false;
    }


}
