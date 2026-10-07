package com.example.java1_2023_rab0046;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class LevelController {
    private Timeline guardTimeline;
    private Level level;
    private Player player;
    private List<Guard> guards;
    private AnchorPane rootPane;


    public LevelController(AnchorPane rootPane) {
        this.rootPane = rootPane;
        this.guards = new ArrayList<>();  //initialize the guards list
    }


    public void initialize(Level level) {
        this.level = level;
        this.player = level.getPlayer();
        this.player.setLevel(this.level);

        guards.add(new Guard(550, 300));
        guards.add(new Guard(50, 180));

        ImageView playerView = player.getSprite();
        ImageView guardView1 = guards.get(0).getSpriteView();
        ImageView guardView2 = guards.get(1).getSpriteView();

        playerView.setLayoutX(player.getX());
        playerView.setLayoutY(player.getY());

        rootPane.getChildren().addAll(playerView, guardView1, guardView2);

        rootPane.setStyle("-fx-background-color: black;");
        Scene scene = new Scene(rootPane, 600, 400);
        scene.setOnKeyPressed(this::handleKeyPress);
        scene.getRoot().requestFocus();

        Stage stage = new Stage();
        stage.setTitle("Lode Runner Game");
        stage.setScene(scene);
        stage.show();

        player.xProperty().addListener((observable, oldValue, newValue) -> playerView.setLayoutX(newValue.doubleValue()));
        player.yProperty().addListener((observable, oldValue, newValue) -> playerView.setLayoutY(newValue.doubleValue()));
        guards.get(0).xProperty().addListener((observable, oldValue, newValue) -> guardView1.setLayoutX(newValue.doubleValue()));
        guards.get(0).yProperty().addListener((observable, oldValue, newValue) -> guardView1.setLayoutY(newValue.doubleValue()));
        guards.get(1).xProperty().addListener((observable, oldValue, newValue) -> guardView2.setLayoutX(newValue.doubleValue()));
        guards.get(1).yProperty().addListener((observable, oldValue, newValue) -> guardView2.setLayoutY(newValue.doubleValue()));

        rootPane.getChildren().add(level.getCanvas());

        //time line for the guard
        guardTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    for (Guard guard : guards) {
                        guard.move(level);
                    }
                })
        );
        guardTimeline.setCycleCount(Timeline.INDEFINITE);
        guardTimeline.play();
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                player.moveLeft();
                level.checkAndCollectGold();
                break;
            case RIGHT:
                player.moveRight();
                level.checkAndCollectGold();
                break;
            case UP:
                player.moveUp();
                level.checkAndCollectGold();
                break;
            case DOWN:
                player.moveDown();
                level.checkAndCollectGold();
                break;
            case E:
                System.out.println("Digging right");
                player.dig(Player.Direction.RIGHT);
                break;
            case Q:
                System.out.println("Digging left");
                player.dig(Player.Direction.LEFT);
                break;
        }
        level.checkAndCollectGold();
        level.checkPortalInteraction();

        if (isGameEnded()) {
            //message to endGame method
            String endMessage = "Game Over!";
            if (isPlayerAtPortal()) {
                endMessage = "Player reached the portal!";
            } else if (isPlayerCaughtByGuard()) {
                endMessage = "Player caught by guard!";
            }
            endGame(endMessage);
        }
    }



    //ending
    private boolean isGameEnded() {
        return isPlayerAtPortal() || isPlayerCaughtByGuard() || isGuardAtPortal();
    }

    private boolean isPlayerAtPortal() {
        return player.hasEnoughGold()
                && Math.abs(player.getX() - level.getPortal().getX()) < Level.BLOCK_SIZE
                && Math.abs(player.getY() - level.getPortal().getY()) < Level.BLOCK_SIZE;
    }

    private boolean isPlayerCaughtByGuard() {
        for (Guard guard : guards) {
            if (Math.abs(player.getX() - guard.getX()) < Level.BLOCK_SIZE &&
                    Math.abs(player.getY() - guard.getY()) < Level.BLOCK_SIZE) {
                return true;
            }
        }
        return false;
    }

    private boolean isGuardAtPortal() {
        for (Guard guard : guards) {
            if (Math.abs(guard.getX() - level.getPortal().getX()) < Level.BLOCK_SIZE &&
                    Math.abs(guard.getY() - level.getPortal().getY()) < Level.BLOCK_SIZE) {
                return true;
            }
        }
        return false;
    }
    
    public void endGame(String message) {
        guardTimeline.stop(); //stop guard movement
        level.setGameOver(true);

        //save
        FileUtils.saveGoldCounterToFile(player.getGoldCounter());

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


}
