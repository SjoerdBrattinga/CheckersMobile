package com.example.checkersmobile;

import android.util.Log;

public abstract class Piece {
    private final String TAG = "Piece";
    private Position position;
    private final Color color;
    private int maxJumps;

    public Piece(Position position, Color color) {
        this.position = position;
        this.color = color;
        this.maxJumps = 0;
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

    public int getMaxJumps() {
        return maxJumps;
    }

    public void setMaxJumps(int maxJumps) {
        this.maxJumps = maxJumps;
    }

    public void move(GameState game, Move move){
        game.setPiece(null, position);
        setPosition(move.getDestination());
        game.setPiece(this, position);

        if(move.isJump()){
            game.setPiece(null, move.getInBetween());
        }
    }

    public boolean isMoveLegal(GameState gameState, Move move){
        return move.isDiagonal()
                && gameState.getPiece(move.getDestination()) == null
                && ( move.distance() == 1
                || ( move.distance() == 2
                && gameState.getPiece(move.getInBetween()) != null
                && gameState.getPiece(move.getInBetween()).getColor() == this.getColor().getOpponent()));
    }
}
