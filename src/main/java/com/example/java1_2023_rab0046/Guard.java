package com.example.java1_2023_rab0046;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Guard {
    private int x, y;
    private ImageView spriteView;
    private boolean movingRight = true;
    private static final int GUARD_SPEED = 25; //speed

    private DoubleProperty xProperty = new SimpleDoubleProperty();
    private DoubleProperty yProperty = new SimpleDoubleProperty();

    public Guard(int x, int y) {
        this.x = x;
        this.y = y;
        Image sprite = new Image(getClass().getResourceAsStream("/pictures/guardR.png"));
        spriteView = new ImageView(sprite);
        spriteView.setFitWidth(25);
        spriteView.setFitHeight(25);
        updatePosition(); //position
    }

    public void move(Level level) {
        if (level.isGameOver()) {
            return;
        }
        if (movingRight) {
            if (canMoveRight(level)) {
                x += GUARD_SPEED;
                updatePosition();
            } else {
                movingRight = false;
                updateSprite();
            }
        } else {
            if (canMoveLeft(level)) {
                x -= GUARD_SPEED;
                updatePosition();
            } else {
                movingRight = true;
                updateSprite();
            }
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public DoubleProperty xProperty() {
        return xProperty;
    }

    public DoubleProperty yProperty() {
        return yProperty;
    }

    private void updateSprite() {
        Image sprite = movingRight
                ? new Image(getClass().getResourceAsStream("/pictures/guardR.png"))
                : new Image(getClass().getResourceAsStream("/pictures/guardL.png"));
        spriteView.setImage(sprite);
    }

    private void updatePosition() {
        spriteView.setLayoutX(x);
        spriteView.setLayoutY(y);
        xProperty.set(x);
        yProperty.set(y);
    }

    private boolean canMoveRight(Level level) {
        int nextX = x + GUARD_SPEED;
        return level.isWithinStageBoundaries(nextX, y) && !level.isColliding(nextX, y) && level.isBlockBelow(nextX, y);
    }

    private boolean canMoveLeft(Level level) {
        int nextX = x - GUARD_SPEED;
        return level.isWithinStageBoundaries(nextX, y) && !level.isColliding(nextX, y) && level.isBlockBelow(nextX, y);
    }

    public ImageView getSpriteView() {
        return spriteView;
    }
}
