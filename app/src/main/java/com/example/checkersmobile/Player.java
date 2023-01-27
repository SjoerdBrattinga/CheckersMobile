package com.example.checkersmobile;

public class Player {
    final private Color color;
    final private String name;

    public Player(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
