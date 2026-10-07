package com.example.java1_2023_rab0046;

import javafx.scene.image.Image;

public class Gold {
    private double x, y;
    private boolean collected;
    private Image goldImage;

    public Gold(double x, double y) {
        this.x = x;
        this.y = y;
        this.collected = false;
        this.goldImage = new Image(getClass().getResourceAsStream("/pictures/Gold.png"));
    }

    //collect gold
    public void collect() {
        this.collected = true;
    }

    //getters
    public boolean isCollected() {
        return collected;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Image getGoldImage() {
        return goldImage;
    }
}
