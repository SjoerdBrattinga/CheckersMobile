package com.example.checkersmobile;

import android.util.Log;

import java.util.Arrays;
import java.util.ArrayList;


public class GameState {
    private final String TAG = "GameState";
    private final int boardSize = 8;
    private final int startingRows = 3;
    private Color currentPlayer = Color.LIGHT;
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
        this.board[0][3] = null;
        this.board[1][4] = new Man(new Position(1,4), Color.DARK);
        this.board[0][7] = null;
        this.board[3][2] = new Man(new Position(3,2), Color.DARK);
        this.board[3][4] = new Man(new Position(3,4), Color.DARK);
        this.board[5][2] = new Man(new Position(5,2), Color.DARK);
        this.board[1][4] = null;
        this.board[1][0] = null;
    }

    public void testBoard2(){
        this.board[4][1] = null;
        this.board[2][5] = null;
        this.board[2][1] = null;
        this.board[0][3] = null;
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
        return getPiece(move.getCurrent()).getColor() == getCurrentPlayer()
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

    public Color getCurrentPlayer() {
        return currentPlayer;
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

    public ArrayList<Move> getPossibleMoves(Color player) {

        ArrayList<Move> possibleMoves = new ArrayList<>();

        if (lastMove == null) {
            ArrayList<Position> piecePositions = getPiecePositions(player);

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
                    && getPiece(move.getInBetween()).getColor() != currentPlayer
                    && getPiece(move.getCurrent()).isMoveLegal(this, move)){
                return true;
            }
        }

        return false;
    }

    public ArrayList<Move> getPossibleMoves2(){
        Log.d(TAG, "getPossibleMoves2: ");
        //ArrayList<Position> playerPiecePositions = getPiecePositions(turn);
        ArrayList<Piece> playerPieces = getPieces(currentPlayer);
        ArrayList<Move> possibleMoves = new ArrayList<>();
        getMaxJumps2(new ArrayList<>(), getPiece(6,1).getPosition());
        for (Piece piece : playerPieces) {
            int jumps = getMaxJumps(piece, piece.getPosition());
            piece.setMaxJumps(jumps);
            this.maxJumps = 0;
        }
        return possibleMoves;
    }

    int maxJumps = 0;
    public int getMaxJumps(Piece piece, Position start) {
        //Piece piece = getPiece(start);
        ArrayList<Move> possibleJumps = getPossibleJumps(start);

        if(!possibleJumps.isEmpty()){
            maxJumps++;

            for (Move move : possibleJumps) {
                exploreJump(piece, move);

                getMaxJumps(piece, move.getDestination());
                undoLastMove(move);
            }
        }

        return maxJumps;
    }

    public int getMaxJumps2(ArrayList<Move> jumpsSoFar, Position start) {
        Piece piece = getPiece(start);
        ArrayList<Move> possibleJumps = getPossibleJumps(start);

        if(!possibleJumps.isEmpty()){
            maxJumps++;
            //jumpsSoFar.addAll(possibleJumps);

            for (Move move : possibleJumps) {
                exploreJump(piece, move);
                if(!jumpsSoFar.contains(move)){
                    jumpsSoFar.add(move);
                }
                getMaxJumps2(jumpsSoFar, move.getDestination());
                undoLastMove(move);
            }
        } else {
            if(!jumpsSoFar.isEmpty()){
                if (jumpsSoFar.size() > piece.getPossibleMoves().size()){
                    piece.setPossibleMoves((ArrayList<Move>) jumpsSoFar.clone());
                    jumpsSoFar.remove(jumpsSoFar.size() - 1);
                } else {
                    jumpsSoFar.remove(jumpsSoFar.size() - 1);
                }

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
        return !getPossibleMoves(currentPlayer).isEmpty();
    }

    public void endTurn(){
        lastMove = null;
        currentPlayer = currentPlayer.getOpponent();
    }

    public boolean isGameOver(){
        ArrayList<Position> opponentPieces = getPiecePositions(currentPlayer);
        
        if(opponentPieces.isEmpty() || getPossibleMoves(currentPlayer).isEmpty()){
            return true;
        }

        return false;
    }

}
