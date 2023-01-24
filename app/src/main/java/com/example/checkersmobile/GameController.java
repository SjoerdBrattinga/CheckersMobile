package com.example.checkersmobile;

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

    public void handleInput(Position selected){
        if (!gameState.isEmptyTile(selected)) {
            onSelectPiece(selected);
        } else if (selectedPiece != null) {
            onSelectTile(selected);
        }
    }

    private void onSelectPiece(Position selected){
        if (!isValidPiece(selected)) {
            return;
        }

        if (selectedPiece == null) {
            setSelectedPiece(selected);
        } else if (selectedPiece.getPosition().equals(selected)) {
            resetSelectedPiece();
        } else {
            changeSelectedPiece(selected);
        }
    }

    private void onSelectTile(Position selected){
        if(!getMoveDestinationPositions(possibleMoves).contains(selected)){
            return;
        }

        if(makeMove(new Move(selectedPiece.getPosition(), selected))){
            updateBoard();
            activity.resetTileHighlights();

            if(gameState.playerHasMoves()){
                getPossibleMoves();
                changeSelectedPiece(selected);
            } else {
                selectedPiece = null;
                endTurn();
            }
        }
    }

    private void newGame(){
        int boardSize = gameState.getBoardSize();
        activity.drawBoard(boardSize,boardSize);
        activity.setCurrentPlayerText(getCurrentPlayer().toString());

        updateBoard();
        getPossibleMoves();
    }

    private void getPossibleMoves(){
        possibleMoves = gameState.getPossibleMoves(gameState.getCurrentPlayer());
        activity.highlightTiles(getPiecePositions(possibleMoves), TileResource.GREEN);
    }


    private boolean makeMove(Move move){
        if (gameState.isMoveLegal(move)) {
            gameState.movePiece(move);
            return true;
        }
        return false;
    }

    private void endTurn(){
        gameState.endTurn();

        if (gameState.isGameOver()){
            activity.setPlayerWinsText(getCurrentPlayer().getOpponent());
            //open game over activity
        } else {
            activity.setCurrentPlayerText(getCurrentPlayer().toString());
            getPossibleMoves();
        }
    }

    private void updateBoard(){
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

    private boolean isValidPiece(Position piecePosition){
        return gameState.getPiece(piecePosition).getColor() == gameState.getCurrentPlayer()
                && getPiecePositions(possibleMoves).contains(piecePosition);

    }

    private void changeSelectedPiece(Position selected) {
        selectedPiece = null;
        onSelectPiece(selected);
    }

    private void setSelectedPiece(Position selected) {
        activity.resetTileHighlights();
        selectedPiece = gameState.getPiece(selected);
        activity.highLightTile(selected, TileResource.BLUE);
        highLightPossibleMovesForSelectedPiece();
    }

    private void resetSelectedPiece() {
        selectedPiece = null;
        activity.resetTileHighlights();
        getPossibleMoves();
    }

    private void highLightPossibleMovesForSelectedPiece(){
        ArrayList<Move> possibleMoves = gameState.getPossibleMoves(selectedPiece);

        if (!possibleMoves.isEmpty()) {
            activity.highlightTiles(getMoveDestinationPositions(possibleMoves), TileResource.GREEN);
        }
    }

    private ArrayList<Position> getMoveDestinationPositions(ArrayList<Move> moves) {
        ArrayList<Position> destinationPositions = new ArrayList<>();

        for (int i = 0; i < moves.size(); i++) {
            if(!destinationPositions.contains(moves.get(i).getDestination())){
                destinationPositions.add(moves.get(i).getDestination());
            }
        }

        return destinationPositions;
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

    private Color getCurrentPlayer(){
        return gameState.getCurrentPlayer();
    }
}
