package com.example.checkersmobile;

import java.util.ArrayList;

public class GameState {
    private final int boardSize = 8;
    private Player lightPlayer, darkPlayer;
    private Color currentPlayer = Color.LIGHT;
    private Piece[][] board;
    private Move lastMove;
    private boolean pieceCrowned;

    public GameState(Player lightPlayer, Player darkPlayer){
        this.lightPlayer = lightPlayer;
        this.darkPlayer = darkPlayer;

        initBoard();
    }

    private void initBoard(){
        board = new Piece[boardSize][boardSize];
        int startingRows = 3;

        for (int row = 0; row < boardSize; row++){
            for (int col = 0; col < boardSize; col++){
                if((row + col) % 2 == 1) {
                    if (row < startingRows){
                        board[row][col] = new Man(new Position(row,col), Color.DARK);
                    } else if (row >= boardSize - startingRows){
                        board[row][col] = new Man(new Position(row,col), Color.LIGHT);
                    }
                }
            }
        }
        //testBoard();
    }

    private ArrayList<Position> getPiecePositions(Color color){
        ArrayList<Position> piecePositions = new ArrayList<>();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == null || board[row][col].getColor() != color) {
                    continue;
                }
                piecePositions.add(new Position(row, col));
            }
        }

        return piecePositions;
    }

    public void setPiece(Piece piece, Position position){
        board[position.getRow()][position.getCol()] = piece;
    }

    /*
     * Only use this after checking if move is legal
     */
    public void movePiece(Move move){
        Piece piece = getPiece(move.getCurrent());
        piece.move(this, move);
        lastMove = move;
    }

    public boolean isMoveLegal(Move move){
        Piece piece = getPiece(move.getCurrent());

        return piece.getColor() == getCurrentPlayer()
                && isMoveWithinBoardBounds(move)
                && isEmptyTile(move.getDestination())
                && piece.isMoveLegal(this, move);
    }

    private boolean isMoveWithinBoardBounds(Move move){
        int[] coordinates = new int[]{
                move.getCol1(),
                move.getRow1(),
                move.getCol2(),
                move.getRow2()
        };

        for (int coordinate : coordinates)
            if(coordinate < 0 || coordinate >= getBoardSize()){
                return false;
            }

        return true;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    //Used in GameControllers updateBoard method to avoid creating a new position object every iteration
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

    public ArrayList<Move> getAllPossibleMoves() {

        ArrayList<Move> possibleMoves = new ArrayList<>();

        if (lastMove == null) {
            ArrayList<Position> piecePositions = getPiecePositions(currentPlayer);

            for (Position position : piecePositions) {
                possibleMoves.addAll(getPossibleJumps(position));
            }

            if(possibleMoves.isEmpty()){
                for (Position position : piecePositions) {
                    possibleMoves.addAll(getAllPossibleMoves(position));
                }
            }

        } else if (lastMove.isJump()){
            possibleMoves.addAll(getPossibleJumps(lastMove.getDestination()));
        }

        return possibleMoves;
    }

    private ArrayList<Move> getPossibleJumps(Position piecePosition) {
        ArrayList<Move> possibleMoves = new ArrayList<>();

        if(canJump(piecePosition)){
            Position[] destinations = piecePosition.getDiagonal(2);

            for (Position destination : destinations) {

                Move move = new Move(piecePosition, destination);

                if(isMoveLegal(move)) {
                    possibleMoves.add(move);
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<Move> getAllPossibleMoves(Position piecePosition) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        Position[] destinations;

        if(canJump(piecePosition)){
            destinations = piecePosition.getDiagonal(2);
        } else {
            destinations = piecePosition.getDiagonal(1);
        }

        for (Position destination : destinations) {
            Move move = new Move(piecePosition, destination);

            if(isMoveLegal(move)) {
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    private boolean canJump(Position position){
        Position[] destinations = position.getDiagonal(2);

        for (Position destination : destinations) {
            Move move = new Move(position, destination);

            if (isMoveLegal(move)){
                return true;
            }
        }

        return false;
    }

    public boolean playerHasMoves(){
        return !pieceCrowned && !getAllPossibleMoves().isEmpty();
    }

    public void setPieceCrowned(boolean pieceCrowned) {
        this.pieceCrowned = pieceCrowned;
    }

    public void endTurn(){
        lastMove = null;
        pieceCrowned = false;
        currentPlayer = currentPlayer.getOpponent();
    }

    public boolean isGameOver(){
        ArrayList<Position> pieces = getPiecePositions(currentPlayer);

        return pieces.isEmpty() || getAllPossibleMoves().isEmpty();
    }



    //test functions

    private void initTestBoard(){
        board[3][4] = new King(new Position(3,4), Color.DARK);
        board[3][6] = new King(new Position(3,6), Color.DARK);
        board[5][4] = new King(new Position(5,4), Color.LIGHT);
        board[5][6] = new King(new Position(5,6), Color.LIGHT);
    }

    private void testBoard(){
        this.board[0][3] = null;
        this.board[0][5] = null;
        this.board[0][7] = null;
        this.board[1][0] = null;
        this.board[1][4] = new Man(new Position(1,4), Color.DARK);
        this.board[2][1] = null;
        this.board[2][5] = null;
        this.board[3][2] = new Man(new Position(3,2), Color.DARK);
        this.board[3][4] = new Man(new Position(3,4), Color.DARK);
        this.board[4][1] = null;
        this.board[5][2] = new Man(new Position(5,2), Color.DARK);
    }

    private void testBoard2(){
        this.board[0][3] = null;
        this.board[1][0] = null;
        this.board[1][4] = null;
        this.board[2][1] = null;
        this.board[2][5] = null;
        this.board[3][2] = new Man(new Position(3,2), Color.DARK);
        this.board[3][4] = new Man(new Position(3,4), Color.DARK);
        this.board[4][1] = null;
        this.board[5][2] = new Man(new Position(5,2), Color.DARK);
    }
}

