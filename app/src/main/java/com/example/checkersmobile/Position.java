package com.example.checkersmobile;

import androidx.annotation.NonNull;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    private Position getUpLeft(int distance){
        return new Position(row - distance, col - distance);
    }

    private Position getUpRight(int distance){
        return new Position(row - distance, col + distance);
    }

    private Position getDownLeft(int distance){
        return new Position(row + distance, col - distance);
    }

    private Position getDownRight(int distance){
        return new Position(row + distance, col + distance);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position[] getDiagonal(int distance){
        return new Position[]{
                getUpLeft(distance),
                getUpRight(distance),
                getDownLeft(distance),
                getDownRight(distance)
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        return row == position.row && col == position.col;
    }

    @NonNull
    @Override
    public String toString(){
        return "row:" + this.row + " col:" + this.col;
    }

}
