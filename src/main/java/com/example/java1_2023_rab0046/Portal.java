package com.example.java1_2023_rab0046;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Portal {
    private double x;
    private double y;
    private boolean activated;
    private Image portalImage;

    public Portal(double x, double y) {
        this.x = x;
        this.y = y;
        this.activated = false;
        this.portalImage = new Image(getClass().getResourceAsStream("/pictures/Portal.png"));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isActivated() {
        return activated;
    }

    public void activate() {
        activated = true;
    }

    public void draw(GraphicsContext gc) {
        if (!activated) {
            //draw image
            gc.drawImage(portalImage, x, y, Level.BLOCK_SIZE, Level.BLOCK_SIZE);
        }
    }
}
