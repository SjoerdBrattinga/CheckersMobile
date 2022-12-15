package com.example.checkersmobile;

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

    public Position[] getConnected(){
        return new Position[]{
                getUpLeft(),
                getUpRight(),
                getDownLeft(),
                getDownRight()
        };
    }

    public Position getUpLeft(){
        return new Position(row + 1, col - 1);
    }

    public Position getUpRight(){
        return new Position(row + 1, col + 1);
    }

    public Position getDownLeft(){
        return new Position(row - 1, col - 1);
    }

    public Position getDownRight(){
        return new Position(row - 1, col + 1);
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

    @Override
    public String toString(){
        return "row:" + this.row + " col:" + this.col;
    }

}
