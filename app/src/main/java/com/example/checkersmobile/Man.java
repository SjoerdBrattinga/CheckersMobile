package com.example.checkersmobile;

public class Man extends Piece{

    public Man(Position position, Color color) {
        super(position, color);
    }

    @Override
    public void move(GameState gameState, Move move) {
        super.move(gameState, move);

        int backRow = getColor() == Color.LIGHT ? 0 : 7;

        if (move.getRow2() == backRow){
            makeKing(gameState);
        }
    }

    @Override
    public boolean isMoveLegal(GameState gameState, Move move) {
        return super.isMoveLegal(gameState, move)
                && ( (getColor() == Color.LIGHT && move.isUp())
                || (getColor() == Color.DARK && move.isDown()));
    }

    private void makeKing(GameState gameState){
        gameState.setPiece(new King(getPosition(),getColor()), getPosition());
        gameState.setPieceCrowned(true);
    }
}
