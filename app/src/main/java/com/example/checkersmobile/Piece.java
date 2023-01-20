package com.example.checkersmobile;

public abstract class Piece {
    private final String TAG = "Piece";
    private Position position;
    private final Color color;

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

    public boolean isMoveLegal(GameState gameState, Move move){
        return move.isDiagonal()
                && gameState.getPiece(move.getDestination()) == null
                && ( move.distance() == 1
                || ( move.distance() == 2
                && gameState.getPiece(move.getInBetween()) != null
                && gameState.getPiece(move.getInBetween()).getColor() == this.getColor().getOpponent()));
    }
}
