package com.example.checkersmobile;

public class GameState {
    private final int boardSize = 8;
    private Piece[][] board;

    public GameState(){
        this.board = new Piece[boardSize][boardSize];
    }
}
