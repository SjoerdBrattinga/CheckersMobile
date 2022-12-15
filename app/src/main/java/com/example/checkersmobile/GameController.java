package com.example.checkersmobile;

import android.util.Log;

import java.util.ArrayList;

public class GameController {
    private final String TAG = "GameController";
    private GameState gameState;
    private BoardActivity activity;
    private Piece selectedPiece;

    

    public GameController(GameState gameState, BoardActivity activity){
        this.gameState = gameState;
        this.activity = activity;
        newGame();
    }

    public void handleInput(Position selected){
        Log.d(TAG, "handleInput: " + selected.toString());

        if(gameState.isEmptyTile(selected)){
            if(selectedPiece != null){
                onSelectTile(selected);
            }

        } else {
            onSelectPiece(selected);
        }
//        if (selectedPiece == null){
//            if(gameState.getPiece(selected) != null && gameState.getPiece(selected).getColor() == gameState.getTurn()){
//                selectedPiece = gameState.getPiece(selected);
//            }
//        } else {
//            if (selectedPiece.getPosition().equals(selected)){
//                selectedPiece = null;
//            } else if (gameState.isEmptyTile(selected)) {
//                //Log.d(TAG, "onClick: to = " + row + "," + col);
//                activity.makeMove(new Move(selectedPiece.getPosition(), selected));
//            }
//        }
    }

    private void onSelectPiece(Position selected){

        //check if piece is of current player
        if(gameState.getPiece(selected).getColor() == gameState.getTurn()){
            if(selectedPiece == null){
                //check if piece has moves
                ArrayList<Move> possibleMoves = gameState.getPossibleMoves(gameState.getPiece(selected));
                if(possibleMoves.size() > 0 ){
                    selectedPiece = gameState.getPiece(selected);
                    ArrayList<Position> tilesToHighlight =new ArrayList<>();

                    for (int i = 0; i < possibleMoves.size(); i++) {
                        tilesToHighlight.add(possibleMoves.get(i).getDestination());
                    }

                    activity.setHighlightedTiles(tilesToHighlight);
                }

            } else if (selectedPiece.getPosition().equals(selected)){
                selectedPiece = null;
            }
        }
    }

    private void onSelectTile(Position selected){
        Log.d(TAG, "onSelectTile: " + selected.toString());
        if(makeMove(new Move(selectedPiece.getPosition(), selected))){
            selectedPiece = null;
            updateBoard();
        }

    }

    public void newGame(){
        //this.gameState = new GameState();
        int boardSize = gameState.getBoardSize();
        activity.drawBoard(boardSize,boardSize);
        updateBoard();
        //activity.setCurrentPlayerText();

        //possibleMoves = controller.getMovablePiecePositions();
        //highlightTiles(possibleMoves, TileResource.BLUE);
    }

    public Color getCurrentPlayer(){
        return gameState.getTurn();
    }

    public int getBoardSize(){
        return gameState.getBoardSize();
    }

    public boolean makeMove(Move move){
        if (gameState.isMoveLegal(move)){
            gameState.movePiece(move);
            return true;

        } else{
            return false;
        }
        //activity.updateBoard();

//        if (gameState.getPossibleMoves().size() > 0){
//
//        }


//        if (!gameState.playerHasMoves()){
//            endTurn();
//        }
    }

    public void getPiece(Position selected){
        //return gameState.getPiece(selected);
    }

    public void updateBoard(){
        //Piece[][] board = gameState.getBoard();

        for (int i = 0; i < gameState.getBoardSize(); i++) {
            for (int j = 0; j < gameState.getBoardSize(); j++) {
                if(gameState.getPiece(i,j) == null){
                    activity.removePiece(i,j);
                } else if(gameState.getPiece(i,j).getColor() == Color.LIGHT){
                    activity.drawLightPiece(i,j);
                } else if (gameState.getPiece(i,j).getColor() == Color.DARK){
                    activity.drawDarkPiece(i,j);
                }
            }
        }
    }

    public void getPossibleMoves(){
        //ArrayList<Move> moves = gameState.getPossibleMoves();

        //return moves;
    }



    public void endTurn(){
        gameState.switchTurn();
    }

    public void saveGameState(){

    }


}
