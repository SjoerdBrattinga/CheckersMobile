package com.example.checkersmobile;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Move {
    private final Position current, destination;

    public Move(Position start, Position destination) {
        this.current = start;
        this.destination = destination;
    }
    public Position getCurrent() {
        return current;
    }
    public Position getDestination() {
        return destination;
    }

    public int getCol1() {
        return getCurrent().getCol();
    }
    public int getRow1() {
        return getCurrent().getRow();
    }
    public int getCol2() {
        return getDestination().getCol();
    }
    public int getRow2() {
        return getDestination().getRow();
    }

    public boolean isUp() {
        return getRow2() - getRow1() < 0;
    }
    public boolean isDown() {
        return getRow2() - getRow1() > 0;
    }

    public boolean isDiagonal() {
        return Math.abs(getRow2() - getRow1()) == Math.abs(getCol2() - getCol1());
    }
    public boolean isJump(){
        return  isDiagonal() && distance() == 2;
    }

    public Position getInBetween(){
        int row = (getRow2() + getRow1()) / 2;
        int col = (getCol2() + getCol1()) / 2;

        return new Position(row,col);
    }

    public int distance() {
        return Math.max(
                Math.abs(getRow2() - getRow1()),
                Math.abs(getCol2() - getCol1())
        );
    }

    @NonNull
    @Override
    public String toString(){
        return "current: " + this.current.toString() + " destination:" + this.destination.toString();
    }


}
