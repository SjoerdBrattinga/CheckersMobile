package com.example.checkersmobile;

import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.ArrayList;


public class GameState {
    private final String TAG = "GameState";
    private final int boardSize = 8;
    private final int startingRows = 3;
    private Color turn = Color.LIGHT;
    private Piece[][] board;
    private Move lastMove;
    private ArrayList<Piece> capturedPieces;
    private ArrayList<Move> playerMoves;

    public GameState(){
        board = new Piece[boardSize][boardSize];
        capturedPieces = new ArrayList<>();
        playerMoves = new ArrayList<>();
        initBoard(board);
    }

    private void initBoard(Piece[][] board){
        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                if((i + j) % 2 == 1) {
                    if (i < startingRows){
                        board[i][j] = new Man(new Position(i,j), Color.DARK);
                    } else if (i >= boardSize - startingRows ){
                        board[i][j] = new Man(new Position(i,j), Color.LIGHT);
                    }
                }
            }
        }
        testBoard();
        //printBoard();
    }

    public void testBoard(){
        this.board[4][1] = null;
        this.board[2][5] = null;
        this.board[2][1] = null;
        this.board[0][7] = null;
        this.board[3][2] = new Man(new Position(3,2), Color.DARK);
        this.board[3][4] = new Man(new Position(3,4), Color.DARK);
        this.board[5][2] = new Man(new Position(5,2), Color.DARK);
        this.board[1][4] = null;
        this.board[1][0] = null;
    }

    public void printBoard(){
        String[][] boardStrArr = new String[8][8];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if(board[i][j] == null){
                    boardStrArr[i][j] = "null";
                } else {
                    if(board[i][j] instanceof King){
                        boardStrArr[i][j] = "King";
                    } else {
                        boardStrArr[i][j] = board[i][j].getColor().toString();
                    }
                }
                if(j + 1 == boardSize){
                    Log.d(TAG, Arrays.deepToString(boardStrArr[i]));
                }
            }
        }
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

    public void setPiece(Piece piece, Position position){
        board[position.getRow()][position.getCol()] = piece;
    }

    public void movePiece(Move move){
        getPiece(move.getCurrent()).move(this, move);
        playerMoves.add(move);
        lastMove = move;
    }

    public void addCapturedPiece(Piece piece) {
        if (piece != null) {
            //Piece copy = (Piece) piece.clone();
            //capturedPieces.add(copy);

            capturedPieces.add(piece);
        }

    }

    public boolean isMoveLegal(Move move){
        return getPiece(move.getCurrent()).getColor() == getTurn()
                && isOnBoard(move.getDestination())
                && isOnBoard(move.getCurrent())
                && getPiece(move.getDestination()) == null
                && getPiece(move.getCurrent()).isMoveLegal(this, move);
    }

    public boolean isOnBoard(Position position){
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

    public Piece getPiece(Position position){
        return board[position.getRow()][position.getCol()];
    }

    public Boolean isEmptyTile(Position position){
        return getPiece(position) == null;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public ArrayList<Move> getPossibleMoves() {

        ArrayList<Move> possibleMoves = new ArrayList<>();

        if (lastMove == null) {
            ArrayList<Position> piecePositions = getPiecePositions(turn);

            if (hasJumps(piecePositions)) {
                for (Position position : piecePositions) {
                    ArrayList<Move> test = getPossibleMoves(position, true);
                    possibleMoves.addAll(test);
                }
            } else {
                for (Position position : piecePositions) {
                    possibleMoves.addAll(getPossibleMoves(position, false));
                }
            }
        } else if (lastMove.isJump()){
            possibleMoves.addAll(getPossibleMoves(lastMove.getDestination(), true));
        }
        //this.possibleMoves = possibleMoves;
        return possibleMoves;
    }

    public ArrayList<Move> getPossibleMoves(Piece piece) {
        //getPossibleMoves2();
        ArrayList<Move> possibleMoves = new ArrayList<>();
        Position current = piece.getPosition();
        Position[] destinations;

        if(canJump(current)){
            destinations = piece.getPosition().getDiagonal(2);
        } else {
            destinations = piece.getPosition().getDiagonal(1);
        }

        for (Position destination : destinations) {
            Move move = new Move(current, destination);
            if (isMoveLegal(move) && piece.isMoveLegal(this, move)) {
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

        for (Position destination : destinations) {
            Move move = new Move(piecePosition, destination);
            if (isMoveLegal(move) && getPiece(piecePosition).isMoveLegal(this, move)) {
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
            if(     isMoveLegal(move)
                    && move.isJump()
                    && getPiece(move.getInBetween()) != null
                    && getPiece(move.getInBetween()).getColor() != turn
                    && getPiece(move.getCurrent()).isMoveLegal(this, move)){
                return true;
            }
        }

        return false;
    }

    public void getPossibleMoves2(){
        Log.d(TAG, "getPossibleMoves2: ");
        //ArrayList<Position> playerPiecePositions = getPiecePositions(turn);
        ArrayList<Piece> playerPieces = getPieces(turn);
        ArrayList<Move> jumpMoves = new ArrayList<>();

        for (Piece piece : playerPieces) {
            int jumps = getMaxJumps(new ArrayList<>(), piece.getPosition());
            piece.setMaxJumps(jumps);
            this.maxJumps = 0;
        }

    }

    int maxJumps = 0;
    public int getMaxJumps(ArrayList<Move> jumps, Position start) {
        Piece piece = getPiece(start);
        ArrayList<Move> possibleJumps = getPossibleJumps(start);

        if(!possibleJumps.isEmpty()){
//            if(!jumps.isEmpty()){
//                //undoLastMove(jumps.get(jumps.size() - 1));
//                //Move remove = jumps.remove(jumps.size() - 1);
//                //undoLastMove(remove);
//            }
//        } else {
            maxJumps++;
           // jumps.addAll(possibleJumps);

            for (Move move : possibleJumps) {
                exploreJump(piece, move);
                jumps.add(move);

                getMaxJumps(jumps, move.getDestination());
                undoLastMove(move);

            }
        }

        return maxJumps;
    }

    private void exploreJump(Piece piece, Move move){
        Log.d(TAG, "explore: move: " + move.toString());

        if(move.isJump()){
            setPiece(null, move.getCurrent());
            setPiece(piece, move.getDestination());

            Piece capturedPiece = getPiece(move.getInBetween());

            if(capturedPiece != null){
                addCapturedPiece(capturedPiece);
            }
            setPiece(null, move.getInBetween());
        }
    }

    public void undoLastMove(Move move){
        setPiece(getPiece(move.getDestination()), move.getCurrent());
        setPiece(null, move.getDestination());

        if(move.isJump() && !capturedPieces.isEmpty()){
            Piece capturedPiece = capturedPieces.get(capturedPieces.size() - 1);
            setPiece(capturedPiece, capturedPiece.getPosition());
            capturedPieces.remove(capturedPiece);
        }
    }

    public ArrayList<Move> getPossibleJumps(Position piecePosition) {
        ArrayList<Move> possibleMoves = new ArrayList<>();

        if(canJump(piecePosition)){
            Position[] destinations = piecePosition.getDiagonal(2);;

            for (Position destination : destinations) {

                Move move = new Move(piecePosition, destination);

                if (isMoveLegal(move) && getPiece(piecePosition).isMoveLegal(this, move)) {
                    possibleMoves.add(move);
                }
            }
        }
        return possibleMoves;
    }

    public boolean playerHasMoves(){
        return !getPossibleMoves().isEmpty();
    }

    public void endTurn(){
        lastMove = null;
        turn = turn.getOpponent();
    }

    public boolean isGameOver(){
        
        ArrayList<Position> opponentPieces = getPiecePositions(turn.getOpponent());
        
        if(opponentPieces.isEmpty()){
            return true;
        }
//        else {
//            boolean noMoreMoves = true;
//            for (Position position :
//                    opponentPieces) {
//                if(!getPossibleMoves(getPiece(position)).isEmpty()){
//                    noMoreMoves = false;
//                }
//            }
//            return noMoreMoves;
//        }
//
        return false;
    }

}
