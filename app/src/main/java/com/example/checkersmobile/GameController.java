package com.example.checkersmobile;

import java.util.ArrayList;

public class GameController {
    private final String TAG = "GameController";
    private final GameState gameState;
    private final BoardActivity activity;
    private Player player1, player2, currentPlayer;
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
        if (isValidPiece(selected)) {
            if (selectedPiece == null) {
                selectedPiece(selected);
            } else if (selectedPiece.getPosition().equals(selected)) {
                undoPieceSelection();
            } else {
                changeSelectedPiece(selected);
            }
        }
    }

    private void onSelectTile(Position selected){
        if (isValidTile(selected)) {
            makeMove(new Move(selectedPiece.getPosition(), selected));
        }
    }

    private void newGame(){
        int boardSize = gameState.getBoardSize();
        activity.drawBoard(boardSize,boardSize);
        activity.setCurrentPlayerText(gameState.getCurrentPlayer().toString());

        updateBoard();
        showPossibleMoves();
    }

    private void showPossibleMoves(){
        possibleMoves = gameState.getAllPossibleMoves();
        activity.highlightTiles(getMovePiecePositions(possibleMoves), TileResource.GREEN);
    }


    private void makeMove(Move move){
        if (gameState.isMoveLegal(move)) {
            gameState.movePiece(move);
            activity.resetTileHighlights();
            updateBoard();

            if (gameState.playerHasMoves()) {
                showPossibleMoves();
                changeSelectedPiece(move.getDestination());
            } else {
                endTurn();
            }
        }
    }

    private void endTurn(){
        selectedPiece = null;
        gameState.endTurn();

        if (gameState.isGameOver()){
            activity.setPlayerWinsText(gameState.getCurrentPlayer().getOpponent());
            activity.gameOverDialog();
        } else {
            activity.setCurrentPlayerText(gameState.getCurrentPlayer().toString());
            showPossibleMoves();
        }
    }

    /*
     * Draw the pieces according to the current game state
     */
    private void updateBoard() {
        for (int row = 0; row < gameState.getBoardSize(); row++) {
            for (int col = 0; col < gameState.getBoardSize(); col++) {
                Piece piece = gameState.getPiece(row,col);

                if(piece == null){
                    activity.removePiece(row, col);
                } else if (piece instanceof King){
                    activity.drawKing(row, col, piece.getColor());
                } else if (piece instanceof Man) {
                    activity.drawMan(row, col, piece.getColor());
                }
            }
        }
    }

    private boolean isValidPiece(Position piecePosition){
        return gameState.getPiece(piecePosition).getColor() == gameState.getCurrentPlayer()
                && getMovePiecePositions(possibleMoves).contains(piecePosition);

    }

    private boolean isValidTile(Position selected) {
        return getMoveDestinationPositions(possibleMoves).contains(selected);
    }

    private void selectedPiece(Position selected) {
        selectedPiece = gameState.getPiece(selected);
        highLightPossibleMovesForSelected(selected);
    }

    private void undoPieceSelection() {
        selectedPiece = null;
        activity.resetTileHighlights();
        showPossibleMoves();
    }

    private void changeSelectedPiece(Position selected) {
        selectedPiece = null;
        onSelectPiece(selected);
    }

    private void highLightPossibleMovesForSelected(Position piecePosition){
        activity.resetTileHighlights();

        ArrayList<Move> possibleMoves = gameState.getAllPossibleMoves(piecePosition);

        if (!possibleMoves.isEmpty()) {
            activity.highLightTile(piecePosition, TileResource.BLUE);
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

    private ArrayList<Position> getMovePiecePositions(ArrayList<Move> moves) {
        ArrayList<Position> piecePositions = new ArrayList<>();

        for (int i = 0; i < moves.size(); i++) {
            if(!piecePositions.contains(moves.get(i).getCurrent())){
                piecePositions.add(moves.get(i).getCurrent());
            }
        }

        return piecePositions;
    }
}
