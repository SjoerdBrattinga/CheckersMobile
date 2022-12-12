package com.example.checkersmobile;

public enum Color {
    LIGHT, DARK;

    public Color getOpponent() {
        return this == LIGHT ? DARK : LIGHT;
    }
}
