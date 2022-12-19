package com.example.checkersmobile;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.ArrayList;


public class GameState {
    private final String TAG = "GameState";
    private final int boardSize = 8;
    private final int startingRows = 3;
    private Color turn = Color.LIGHT;
    private Piece[][] board;
    private ArrayList<Move> moves;

    public GameState(){
        board = new Piece[boardSize][boardSize];
        moves = new ArrayList<>();
        initBoard(board);
        if(canJump(new Position(5,0))){
            Log.d(TAG, "GameState: canjump");
        }
        
        //possibleMoves = getPossibleMoves();
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

        //test
        board[4][1] = new Piece(new Position(4,1), Color.DARK);
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

    public ArrayList<Position> getPiecePositions(Color color){
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

    public ArrayList<Piece> getPieces(Color playerColor){
        ArrayList<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != null && board[i][j].getColor() == playerColor) {
                    pieces.add(board[i][j]);
                }
            }
        }
        return pieces;
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

    public void setPiece(Piece piece, @NonNull Position position){
        board[position.getRow()][position.getCol()] = piece;
    }

    public void movePiece(Move move){
        Log.d(TAG, "movePiece: ");
        Piece piece = getPiece(move.getCurrent());
        if(piece.isMoveLegal(this, move)){
            piece.move(this, move);
            moves.add(move);
            //check capture
        }

//        boolean noMoreMoves = true;
//
//        if(noMoreMoves){
//            switchTurn();
//        }

    }

    public boolean isMoveLegal(@NonNull Move move){
        return getPiece(move.getCurrent()).getColor() == getTurn()
                && isOnBoard(move.getDestination())
                && isOnBoard(move.getCurrent())
                && getPiece(move.getDestination())  == null;
    }

    public boolean isOnBoard(@NonNull Position position){
        int[] coordinates = {
                position.getRow(),
                position.getCol()
        };

        for(int value : coordinates){
            if(value < 0 || value >= boardSize){
                return false;
            }
        }
        return true;
    }

    public Color getTurn() {
        return turn;
    }

    public Piece getPiece(int row, int col){
        return board[row][col];
    }

    public Piece getPiece(@NonNull Position position){
        return board[position.getRow()][position.getCol()];
    }

    public Boolean isEmptyTile(Position position){
        return getPiece(position) == null;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void switchTurn(){
        moves.clear();
        turn = turn.getOpponent();
    }

    public ArrayList<Move> getPossibleMoves() {
        ArrayList<Position> piecePositions = getPiecePositions(turn);

        ArrayList<Move> possibleMoves = new ArrayList<>();

        if (moves.size() > 0 && moves.get(moves.size() - 1).isJump()){
            //get possible moves for last moved piece
            possibleMoves.addAll(getPossibleMoves(moves.get(moves.size() - 1).getDestination(), true));
        } else if (hasJumps(piecePositions)){
            for (Position position : piecePositions) {
                ArrayList<Move> test = getPossibleMoves(position, true);
                possibleMoves.addAll(test);
            }
        } else{
            for (Position position : piecePositions) {
                possibleMoves.addAll(getPossibleMoves(position, false));
            }


//            for (int i = 0; i < playerPieces.size(); i++) {
//                //Position current = playerPieces.get(i).getPosition();
//                possibleMoves.addAll(getPossibleMoves(playerPieces.get(i)));
////                Position[] connected = current.getDiagonal(1);
////
////                for (int j = 0; j < connected.length; j++) {
////                    Move move = new Move(current, connected[j]);
////
////                    if(isMoveLegal(move)){
////                        possibleMoves.add(move);
////                    }
////                }
//            }
        }
        return possibleMoves;
    }

//    public ArrayList<Position> getPossibleMoves() {
//        ArrayList<Position> piecePositions = new ArrayList<>();
//        ArrayList<Piece> playerPieces = getPieces(turn);
//
//        for (int i = 0; i < playerPieces.size(); i++) {
//            if(getPossibleMoves(playerPieces.get(i)).size() > 0){
//                piecePositions.add(playerPieces.get(i).getPosition());
//            }
//        }
//        return piecePositions;
//    }

    private ArrayList<Move> getPossibleMoves4(@NonNull Piece piece) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        Position current = piece.getPosition();
        Position[] destinations;

        if(canJump(current)){
            destinations = piece.getPosition().getDiagonal(2);
        } else {
            destinations = piece.getPosition().getDiagonal(1);
        }

        //Position[] connected = piece.getPosition().getDiagonal(1);
        for (int j = 0; j < destinations.length; j++) {
            Move move = new Move(current, destinations[j]);
            if(isMoveLegal(move) && piece.isMoveLegal(this,move)){
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    private ArrayList<Move> getPossibleMoves(Position piecePosition, boolean jump) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        Position[] destinations;

        if(jump){
            destinations = piecePosition.getDiagonal(2);

        } else {
            destinations = piecePosition.getDiagonal(1);
        }

        for (int j = 0; j < destinations.length; j++) {
            Move move = new Move(piecePosition, destinations[j]);
            if(isMoveLegal(move) && getPiece(piecePosition).isMoveLegal(this,move)){
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    private boolean hasJumps(ArrayList<Position> piecePositions){
        for (Position piece : piecePositions) {
            if (canJump(piece)) return true;
        }
        return false;
    }

    public boolean canJump(Position position){
        Position[] destinations = position.getDiagonal(2);
        for (Position destination : destinations) {
            Move move = new Move(position, destination);

            return isMoveLegal(move)
                    && move.isJump()
                    && getPiece(move.getInBetween()) != null
                    && getPiece(move.getInBetween()).getColor() != turn;
        }

        return false;
    }


    public boolean playerHasMoves(){
        if (moves.size() > 0){
            Move lastMove = moves.get(moves.size() - 1);
            if(canJump(lastMove.getDestination())){
                return true;
            }
            //ArrayList<Move> possibleMoves = getPossibleMoves(getPiece(moves.get(moves.size() - 1).getDestination()));
        } else {
            //check all pieces
        }
        return false;
    }

}
