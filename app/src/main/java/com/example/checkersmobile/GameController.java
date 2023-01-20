package com.example.checkersmobile;

import android.util.Log;

import java.util.ArrayList;

public class GameController {
    private final String TAG = "GameController";
    private GameState gameState;
    private BoardActivity activity;
    private Piece selectedPiece;
    private ArrayList<Move> possibleMoves;

    public GameController(GameState gameState, BoardActivity activity){
        this.gameState = gameState;
        this.activity = activity;
        newGame();
    }

    public void newGame(){
        int boardSize = gameState.getBoardSize();
        activity.drawBoard(boardSize,boardSize);
        updateBoard();

        activity.setCurrentPlayerText(getCurrentPlayer().toString());
        possibleMoves = gameState.getPossibleMoves(gameState.getCurrentPlayer());
        activity.highlightTiles(getPiecePositions(possibleMoves), TileResource.GREEN);
    }

    public void updateBoard(){
        for (int row = 0; row < gameState.getBoardSize(); row++) {
            for (int col = 0; col < gameState.getBoardSize(); col++) {
                if(gameState.getPiece(row,col) == null){
                    activity.removePiece(row,col);
                } else if(gameState.getPiece(row,col).getColor() == Color.LIGHT){
                    if (gameState.getPiece(row,col) instanceof King){
                        activity.drawLightKing(row,col);
                    } else {
                        activity.drawLightPiece(row,col);
                    }
                } else if (gameState.getPiece(row,col).getColor() == Color.DARK){
                    if (gameState.getPiece(row,col) instanceof King){
                        activity.drawDarkKing(row,col);
                    } else {
                        activity.drawDarkPiece(row,col);
                    }
                }
            }
        }
    }

    public void handleInput(Position selected){
        if(gameState.isEmptyTile(selected)){
            if(selectedPiece != null){
                onSelectTile(selected);
            }
        } else {
            onSelectPiece(selected);
        }
    }

    private void onSelectPiece(Position selected){
        if(gameState.getPiece(selected).getColor() == gameState.getCurrentPlayer()){

            if(selectedPiece == null){
                if(getPiecePositions(possibleMoves).contains(selected)){
                    activity.resetTileHighlights();
                    selectedPiece = gameState.getPiece(selected);
                    activity.highLightTile(selected, TileResource.BLUE);
                    ArrayList<Move> possibleMoves = gameState.getPossibleMoves(selectedPiece);

                    if(possibleMoves.size() > 0){
                        activity.highlightTiles(getMoveDestinations(possibleMoves), TileResource.GREEN);
                    }
                }
            } else {
                selectedPiece = null;
                handleInput(selected);
            }
        }
    }

    private void onSelectTile(Position selected){
        if(makeMove(new Move(selectedPiece.getPosition(), selected))){
            updateBoard();
            activity.resetTileHighlights();

            if(gameState.playerHasMoves()){
                activity.highLightTile(selected, TileResource.GREEN);
                possibleMoves = gameState.getPossibleMoves(gameState.getCurrentPlayer());
                onSelectPiece(selected);

            } else {
                selectedPiece = null;
                endTurn();
            }
        }
    }

    public boolean makeMove(Move move){
        if (!gameState.isMoveLegal(move)) {
            return false;
        } else {
            gameState.movePiece(move);
            return true;
        }
    }

    public void endTurn(){
        gameState.endTurn();

        if (gameState.isGameOver()){
            activity.setPlayerWinsText(getCurrentPlayer().getOpponent());
            //open game over activity
        } else {

            possibleMoves = gameState.getPossibleMoves(getCurrentPlayer());
            activity.highlightTiles(getPiecePositions(possibleMoves), TileResource.GREEN);
            activity.setCurrentPlayerText(getCurrentPlayer().toString());
        }

    }

    private ArrayList<Position> getMoveDestinations(ArrayList<Move> moves) {
        ArrayList<Position> moveDestinations = new ArrayList<>();

        for (int i = 0; i < moves.size(); i++) {
            moveDestinations.add(moves.get(i).getDestination());
        }

        return moveDestinations;
    }

    private ArrayList<Position> getPiecePositions(ArrayList<Move> moves) {
        ArrayList<Position> piecePositions = new ArrayList<>();

        for (int i = 0; i < moves.size(); i++) {
            if(!piecePositions.contains(moves.get(i).getCurrent())){
                piecePositions.add(moves.get(i).getCurrent());
            }
        }

        return piecePositions;
    }

    public Color getCurrentPlayer(){
        return gameState.getCurrentPlayer();
    }
}
