package com.example.java1_2023_rab0046;

public class Ladder {
    private int x, y, height;

    public Ladder(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public boolean isAtPosition(double x, double y) {
        int ladderX = getX() / Level.BLOCK_SIZE;
        int ladderY = getY() / Level.BLOCK_SIZE;
        int ladderHeight = getHeight();

        int playerX = (int) (x / Level.BLOCK_SIZE);
        int playerY = (int) (y / Level.BLOCK_SIZE);

        return playerX == ladderX && playerY >= ladderY && playerY < ladderY + ladderHeight;
    }

    public boolean isWithinLadder(int x, int y) {
        return this.x == x && y >= this.y && y < this.y + this.height;
    }
}
