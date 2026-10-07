package com.example.java1_2023_rab0046;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class StartController {

    @FXML
    private Button startButton;

    @FXML
    private void handleStartButton() {
        Level level = new Level();
        AnchorPane rootPane = new AnchorPane();
        LevelController levelController = new LevelController(rootPane);
        levelController.initialize(level);
    }
}
