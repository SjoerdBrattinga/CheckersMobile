package com.example.checkersmobile;

import android.util.Log;

import java.util.Arrays;
import java.util.ArrayList;


public class GameState {
    private final String TAG = "GameState";
    private final int boardSize = 8;
    //private final int startingPieces = 12;
    private final int startingRows = 3;
    private Color turn = Color.LIGHT;
    private Piece[][] board;

    public GameState(){
        board = new Piece[boardSize][boardSize];
        initBoard(board);
    }

    private void initBoard(Piece[][] board){
        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                if((i + j) % 2 == 1) {
                    if (i < startingRows){
                        board[i][j] = new Piece(new Position(i,j), Color.DARK);
                    } else if (i >= boardSize - startingRows ){
                        board[i][j] = new Piece(new Position(i,j), Color.LIGHT);
                    }

                }
            }
        }
        printBoard(board);
    }

    private void printBoard(Piece[][] board){
        String[][] boardStrArr = new String[8][8];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if(board[i][j] == null){
                    boardStrArr[i][j] = "null";
                } else {
                    boardStrArr[i][j] = board[i][j].getColor().toString();
                }
                if(j + 1 == boardSize){
                    Log.d(TAG, Arrays.deepToString(boardStrArr[i]));
                }

            }
        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    public ArrayList<Position> getPiecePositionsByColor(Color color){
        ArrayList<Position> piecePositions = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == null) {
                    continue;
                }
                if (board[i][j].getColor() == color){
                    piecePositions.add(new Position(i,j));
                }
            }
        }
        return piecePositions;
    }

    public ArrayList<Position> getAllPiecePositions(){
        ArrayList<Position> piecePositions = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != null) {
                    piecePositions.add(new Position(i,j));
                }
            }
        }
        return piecePositions;
    }



    public void setPiece(Piece piece, Position position){
        board[position.getRow()][position.getCol()] = piece;
    }

    public void movePiece(Move move){
        Log.d(TAG, "movePiece: ");
        Piece piece = getPiece(move.getCurrent());
        if(piece.isMoveLegal(this, move)){
            Log.d(TAG, "movePiece: isLegal");
            piece.move(this, move);
        }

        boolean noMoreMoves = true;
        if(noMoreMoves){
            switchTurn();
        }

    }

    public boolean isMoveLegal(Move move){
        if (    !isOnBoard(move.getCol1()) ||
                !isOnBoard(move.getCol2()) ||
                !isOnBoard(move.getRow1()) ||
                !isOnBoard(move.getRow2())) {
            return false;
        }
        return true;
    }

    private boolean isOnBoard(int i){
        if (i < 0 || i >= boardSize){
            return false;
        } else {
            return true;
        }


    }

    public Color getTurn() {
        return turn;
    }

    public Piece getPiece(int row, int col){
        return board[row][col];
    }

    public Piece getPiece(Position position){
        return board[position.getRow()][position.getCol()];
    }

    public int getBoardSize() {
        return boardSize;
    }

    private void switchTurn(){
        turn = turn == Color.LIGHT ? Color.DARK : Color.LIGHT;
    }
}
