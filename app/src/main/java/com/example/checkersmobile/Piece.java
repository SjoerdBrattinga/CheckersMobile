package com.example.checkersmobile;

public abstract class Piece {
    private final Color color;
    private Position position;

    public Piece(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    private void setPosition(Position position){
        this.position = position;
    }

    public Color getColor(){
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void move(GameState gameState, Move move){
        //Remove piece at current position
        gameState.setPiece(null, position);
        //update piece position
        setPosition(move.getDestination());
        //place piece at move destination
        gameState.setPiece(this, position);

        if(move.isJump()){
            //Remove captured piece
            gameState.setPiece(null, move.getInBetween());
        }
    }

    public boolean isMoveLegal(GameState gameState, Move move){
        return move.isDiagonal()
                && ( move.distance() == 1 //regular move
                || ( move.distance() == 2 //jump
                && !gameState.isEmptyTile(move.getInBetween())
                && gameState.getPiece(move.getInBetween()).getColor() == this.getColor().getOpponent()));
    }
}
