package com.example.checkersmobile;

public enum TileResource {
    LIGHT(R.drawable.light_tile),
    //DARK(R.drawable.dark_tile),
    //LIGHT(R.color.light),
    DARK(R.color.dark),
    GREEN(R.drawable.green_tile),
    BLUE(R.drawable.blue_tile),
    RED(R.drawable.red_tile);

    public final int resourceId;

    TileResource(int resourceId) {
        this.resourceId = resourceId;
    }
}
