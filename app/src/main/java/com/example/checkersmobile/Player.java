package com.example.checkersmobile;

public class Player {
    final private int id;
    final private String name;
    private Color color;
    private int gamesWon;

    public Player(int id, String name, int gamesWon) {
        this.id = id;
        this.name = name;
        this.gamesWon = gamesWon;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void addWin(){
        gamesWon++;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
