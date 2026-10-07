package com.example.java1_2023_rab0046;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Player implements Movable {
    public enum Direction {
        LEFT, RIGHT
    }

    private static final int MOVE_SPEED = 15;
    public static final int BLOCK_SIZE = 30;
    private ImageView spriteView;
    private DoubleProperty x, y;
    private Level level;
    private Image spriteRight;
    private Image spriteLeft;
    private int goldCounter;

    public Player() {
        spriteRight = new Image(getClass().getResourceAsStream("/pictures/playerR.png"));
        spriteLeft = new Image(getClass().getResourceAsStream("/pictures/playerL.png"));
        spriteView = new ImageView(spriteRight); //initially set to right image
        spriteView.setFitWidth(BLOCK_SIZE);
        spriteView.setFitHeight(BLOCK_SIZE);

        x = new SimpleDoubleProperty(0);
        y = new SimpleDoubleProperty(295);
    }

    private void checkForGameOver() {
        if (y.get() > 300) {
            level.endGame("Game Over: Player fell!");
        }
    }

    @Override
    public void moveLeft() {
        if (level.isGameOver()) return;
        double newX = x.get() - MOVE_SPEED;
        if (!isCollidingWithBlock(newX, y.get())) {
            x.set(newX);
            spriteView.setImage(spriteLeft);
        }
        applyGravityIfNotOnLadder();
        level.checkAndCollectGold();
        if (level.checkPortalInteraction()) {
            level.endGame("Player reached the portal!");
        }
        checkForGameOver();
    }

    @Override
    public void moveRight() {
        if (level.isGameOver()) return;
        double newX = x.get() + MOVE_SPEED;
        if (!isCollidingWithBlock(newX, y.get())) {
            x.set(newX);
            spriteView.setImage(spriteRight);
        }
        applyGravityIfNotOnLadder();
        level.checkAndCollectGold();
        if (level.checkPortalInteraction()) {
            level.endGame("Player reached the portal!");
        }
        checkForGameOver();
    }

    @Override
    public void moveUp() {
        if (level.isGameOver()) return;
        double newY = y.get() - MOVE_SPEED;
        if (level.isOnLadder(x.get(), y.get()) && level.isWithinStageBoundaries(x.get(), newY)) {
            y.set(newY);
        }
        if (level.checkPortalInteraction()) {
            level.endGame("Player reached the portal!");
        } checkForGameOver();
    }

    @Override
    public void moveDown() {
        if (level.isGameOver()) return;
        double newY = y.get() + MOVE_SPEED;
        if (level.isOnLadder(x.get(), y.get()) && level.isWithinStageBoundaries(x.get(), newY)) {
            y.set(newY);
        }
        if (level.checkPortalInteraction()) {
            level.endGame("Player reached the portal!");
        } checkForGameOver();
    }

    private void applyGravityIfNotOnLadder() {
        double newY = y.get() + MOVE_SPEED;
        if (!level.isBlockBelow(x.get(), newY) && !level.isOnLadder(x.get(), newY) && level.isWithinStageBoundaries(x.get(), newY)) {
            y.set(newY);
        }
    }

    public void dig(Direction direction) {
        int playerX = (int) Math.round(x.get() / Level.BLOCK_SIZE);
        int playerY = (int) Math.round(y.get() / Level.BLOCK_SIZE);

        int targetX = direction == Direction.RIGHT ? playerX + 1 : playerX - 1;
        int targetY = playerY + 1;

        if (level != null && level.canDigAt(targetX, targetY)) {
            level.dig(targetX, targetY);
        }
    }

    private boolean isCollidingWithBlock(double newX, double newY) {
        int leftBlock = (int) Math.floor(newX / Level.BLOCK_SIZE);
        int rightBlock = (int) Math.floor((newX + spriteView.getFitWidth()) / Level.BLOCK_SIZE);
        int topBlock = (int) Math.floor(newY / Level.BLOCK_SIZE);
        int bottomBlock = (int) Math.floor((newY + spriteView.getFitHeight()) / Level.BLOCK_SIZE);

        for (int i = topBlock; i <= bottomBlock; i++) {
            for (int j = leftBlock; j <= rightBlock; j++) {
                if (i >= 0 && i < level.getBlocks().length && j >= 0 && j < level.getBlocks()[0].length) {
                    if (level.getBlocks()[i][j]) {
                        return true; //collision detected
                    }
                }
            }
        }
        return false; //no collision
    }


    //getter for goldCounter
    public int getGoldCounter() {
        return goldCounter;
    }

    public void collectGold() {
        goldCounter++;
    }

    public boolean hasEnoughGold() {

        return goldCounter >= 2;
    }

    public void setLevel(Level level) {
        this.level = level;

    }
    public ImageView getSprite() {
        return spriteView;
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public double getX() {
        return x.get();
    }

    public double getY() {
        return y.get();
    }
}
