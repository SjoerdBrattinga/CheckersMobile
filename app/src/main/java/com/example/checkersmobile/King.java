package com.example.checkersmobile;

public class King extends Piece{

    public King(Position position, Color color) {
        super(position, color);
    }

//    @Override
//    public boolean isMoveLegal(GameState gameState, Move move) {
//        return super.isMoveLegal(gameState, move)
//                && gameState.getPiece(move.getInBetween()) != null
//                && move.distance() == 2
//                && gameState.getPiece(move.getInBetween()).getColor() == this.getColor().getOpponent();
//    }
}
