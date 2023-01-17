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
        possibleMoves = gameState.getPossibleMoves();
        activity.highlightTiles(getPiecePositions(possibleMoves), TileResource.GREEN);
    }

    public void updateBoard(){
        for (int i = 0; i < gameState.getBoardSize(); i++) {
            for (int j = 0; j < gameState.getBoardSize(); j++) {
                if(gameState.getPiece(i,j) == null){
                    activity.removePiece(i,j);
                } else if(gameState.getPiece(i,j).getColor() == Color.LIGHT){
                    if (gameState.getPiece(i,j) instanceof King){
                        activity.drawLightKing(i,j);
                    } else {
                        activity.drawLightPiece(i,j);
                    }
                } else if (gameState.getPiece(i,j).getColor() == Color.DARK){
                    if (gameState.getPiece(i,j) instanceof King){
                        activity.drawDarkKing(i,j);
                    } else {
                        activity.drawDarkPiece(i,j);
                    }
                }
            }
        }
    }

    public void handleInput(Position selected){
        //activity.resetTileHighlights();

        if(gameState.isEmptyTile(selected)){
            if(selectedPiece != null){
                onSelectTile(selected);
            }
        } else {
            onSelectPiece(selected);
        }
    }

    private void onSelectPiece(Position selected){
        if(gameState.getPiece(selected).getColor() == gameState.getTurn()){

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
                possibleMoves = gameState.getPossibleMoves();
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
        //gameState.printBoard();

        if (gameState.isGameOver()){
            Log.d(TAG, "endTurn: GameOver! Player " + getCurrentPlayer() + " Wins!");
        } else {
            gameState.endTurn();
            //ArrayList<Move> moves = gameState.getPossibleMoves();
            possibleMoves = gameState.getPossibleMoves();

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
        return gameState.getTurn();
    }
}
