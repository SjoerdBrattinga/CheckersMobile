package com.example.checkersmobile;

public enum TileResource {
    LIGHT(R.drawable.light_tile),
    DARK(R.drawable.dark_tile),
    GREEN(R.drawable.green_tile),
    BLUE(R.drawable.blue_tile),
    RED(R.drawable.red_tile);

    public final int drawableId;

    TileResource(int drawableId) {
        this.drawableId = drawableId;
    }
}
