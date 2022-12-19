package com.example.checkersmobile;

import androidx.annotation.NonNull;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col){
        this.row = row;
        this.col = col;
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

    public Position getUpLeft(int distance){
        return new Position(row - distance, col - distance);
    }

    public Position getUpRight(int distance){
        return new Position(row - distance, col + distance);
    }

    public Position getDownLeft(int distance){
        return new Position(row + distance, col - distance);
    }

    public Position getDownRight(int distance){
        return new Position(row + distance, col + distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (row != position.row) return false;
        if (col != position.col) return false;

        return true;
    }

    @NonNull
    @Override
    public String toString(){
        return "row:" + this.row + " col:" + this.col;
    }

}
