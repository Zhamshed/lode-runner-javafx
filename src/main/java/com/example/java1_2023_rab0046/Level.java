package com.example.java1_2023_rab0046;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.scene.control.Alert;

public class Level {
    public static final int BLOCK_SIZE = 30;
    private Player player;
    private boolean[][] blocks;
    private Canvas canvas;
    private List<Ladder> ladders;
    private List<Gold> golds;
    private List<Guard> guards;
    private Portal portal;
    private Timeline blockRestorationTimeline;
    private boolean gameOver = false;


    public Level() {
        this.player = new Player();
        this.blocks = new boolean[12][20];

        this.ladders = new ArrayList<>();
        this.golds = new ArrayList<>();
        this.guards = new ArrayList<>();

        this.portal = new Portal(550 , 80);

        initializeBlocks();
        initializeLevelElements();
        initializeCanvas();

        //initialize the time line for block restoration
        blockRestorationTimeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> restoreBlocks())
        );
        blockRestorationTimeline.setCycleCount(Timeline.INDEFINITE);
        blockRestorationTimeline.play();
    }

    private void initializeBlocks() {
        for (boolean[] row : blocks) {
            Arrays.fill(row, false);
        }

        int bottomRow = blocks.length - 1;
        for (int i = 0; i < blocks[0].length; i++) {
            blocks[bottomRow][i] = true;
        }

        int blockHeight = 7;
        for (int i = 0; i < blocks[0].length / 2; i++) {
            blocks[blockHeight][i] = true;
        }

        int blockHeight1 = 4;
        for (int i = blocks[0].length / 2; i < blocks[0].length; i++) {
            blocks[blockHeight1][i] = true;
        }

        int blockOnTop = 1;
        for (int i = 0; i < blocks[0].length; i++) {
            blocks[blockOnTop][i] = true;
        }
    }


    private void restoreBlocks() {
        initializeBlocks();
        redrawCanvas();
    }

    private void initializeCanvas() {
        this.canvas = new Canvas(600, 400);
        redrawCanvas();
    }

    //check and collect gold
    public void checkAndCollectGold() {
        if (gameOver) return;
        double playerX = player.getX();
        double playerY = player.getY();

        for (Gold gold : golds) {
            if (!gold.isCollected()) {
                double goldX = gold.getX() * BLOCK_SIZE;
                double goldY = gold.getY() * BLOCK_SIZE;

                if (playerX < goldX + BLOCK_SIZE &&
                        playerX + BLOCK_SIZE > goldX &&
                        playerY < goldY + BLOCK_SIZE &&
                        playerY + BLOCK_SIZE > goldY) {
                    gold.collect();
                    player.collectGold();
                    redrawCanvas();
                }
            }
        }
    }

    public void redrawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j]) {
                    gc.setFill(Color.BROWN);
                    gc.fillRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        for (Ladder ladder : ladders) {
            drawLadder(gc, ladder);
        }

        //draw gold only if it's not collected
        for (Gold gold : golds) {
            if (!gold.isCollected()) {
                gc.drawImage(gold.getGoldImage(),
                        gold.getX() * BLOCK_SIZE,
                        gold.getY() * BLOCK_SIZE,
                        BLOCK_SIZE, BLOCK_SIZE); //draw gold scaled to BLOCK_SIZE
            }
        }

        //draw the gold counter
        gc.setFill(Color.WHITE);
        gc.fillText("Gold: " + player.getGoldCounter(), 10, 399);

        portal.draw(gc);
    }



    private void initializeLevelElements() {
        ladders.add(new Ladder(280, 115, 4));
        ladders.add(new Ladder(300, 200, 5));
        golds.add(new Gold(12, 10));
        golds.add(new Gold(4, 6));
        guards.add(new Guard(280, 115));
        guards.add(new Guard(450, 100));
    }

    public void dig(int x, int y) {
        if (canDigAt(x, y)) {
            blocks[y][x] = false;
            redrawCanvas();
        }
    }

    public boolean canDigAt(int x, int y) {
        //check if a block can be dug
        return x >= 0 && x < blocks[0].length && y >= 0 && y < blocks.length && blocks[y][x];
    }



    public boolean isWithinStageBoundaries(double x, double y) {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        return x >= 0 && x <= 600 - BLOCK_SIZE && y >= 0 && y <= 400 - BLOCK_SIZE;
    }

    private void drawLadder(GraphicsContext gc, Ladder ladder) {
        gc.setFill(Color.BROWN);
        int ladderX = ladder.getX() / BLOCK_SIZE;
        int ladderY = ladder.getY() / BLOCK_SIZE;

        for (int i = 0; i < ladder.getHeight(); i++) {
            gc.fillRect(ladderX * BLOCK_SIZE, (ladderY + i) * BLOCK_SIZE, BLOCK_SIZE, 2);
        }
    }

    public boolean isColliding(double x, double y) {
        int blockX = (int) (x / BLOCK_SIZE);
        int blockY = (int) (y / BLOCK_SIZE);

        // Check bounds
        if (blockX < 0 || blockX >= blocks[0].length || blockY < 0 || blockY >= blocks.length) {
            return false;
        }

        return blocks[blockY][blockX];
    }

    public boolean isBlockBelow(double x, double y) {
        int blockX = (int) (x / BLOCK_SIZE);
        int blockY = (int) ((y + BLOCK_SIZE) / BLOCK_SIZE);
        return blockX >= 0 && blockX < blocks[0].length && blockY < blocks.length && blocks[blockY][blockX];
    }

    public boolean isOnLadder(double x, double y) {
        int playerX = (int) Math.floor(x / BLOCK_SIZE);
        int playerY = (int) Math.floor(y / BLOCK_SIZE);

        for (Ladder ladder : ladders) {
            if (ladder.isAtPosition(playerX * BLOCK_SIZE, playerY * BLOCK_SIZE)) {
                return true;
            }
        }
        return false;
    }

    //getters
    public Player getPlayer() {
        return player;
    }


    public Portal getPortal() {
        return portal;
    }

    public boolean checkPortalInteraction() {
        if (player.hasEnoughGold() && isPlayerAtPortal()) {
            portal.activate();
            redrawCanvas();
            return true;
        }

        return false;
    }

    public void setGameOver(boolean isOver) {
        this.gameOver = isOver;
    }

    private boolean isPlayerAtPortal() {
        return Math.abs(player.getX() - portal.getX()) < BLOCK_SIZE &&
                Math.abs(player.getY() - portal.getY()) < BLOCK_SIZE;
    }

    public void endGame(String message) {
        gameOver = true;
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            //stop game
        });
    }


    public Canvas getCanvas() {
        return canvas; }
    public List<Ladder> getLadders() {
        return ladders; }
    public boolean[][] getBlocks() {
        return blocks;
    }

    public boolean isGameOver() {
        return gameOver;
    }

}

