package com.eric.codejam.utils;

public enum Direction {
    NORTH(0, 1),
    NORTH_EAST(1, 1),
    EAST(1, 0),
    SOUTH_EAST(1, -1),
    SOUTH(0, -1),
    SOUTH_WEST(-1,-1),
    WEST(-1,0),
    NORTH_WEST(-1, 1);
    
    private int deltaX;
    private int deltaY;
    
    
    public int getDeltaX() {
        return deltaX;
    }
    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }
    public int getDeltaY() {
        return deltaY;
    }
    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }
    private Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }
    
    public Direction turn(int quarterTurnsRight) {
        //1 1, 1 0, 1 -1, 0 -1,    -1 -1, -1 0, -1 1, 0 1
        int newDirection = ordinal() + quarterTurnsRight;
        if (newDirection >= 8) {
            newDirection -= 8;
        }
        if (newDirection < 0) {
            newDirection += 8;
        }
        return Direction.values()[newDirection];
    }
}
