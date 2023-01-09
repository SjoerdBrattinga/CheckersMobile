package com.example.checkersmobile;

public class Man extends Piece{

    public Man(Position position, Color color) {
        super(position, color);
    }

    @Override
    public void move(GameState gameState, Move move) {
        super.move(gameState, move);

        int destinationRow = getColor() == Color.LIGHT ? 0 : 7;

        if (move.getRow2() == destinationRow){
            makeKing(gameState);
        }
    }

    @Override public boolean    isMoveLegal(GameState gameState, Move move) {
        return super.isMoveLegal(gameState, move)
                && ( (getColor() == Color.LIGHT && move.isUp())
                || (getColor() == Color.DARK && move.isDown()) );
    }

    public void makeKing(GameState gameState){
        gameState.setPiece(new King(this.getPosition(),this.getColor()), this.getPosition());
    }
}
