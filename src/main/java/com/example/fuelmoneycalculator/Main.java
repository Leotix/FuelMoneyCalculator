package com.example.fuelmoneycalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage){
        VBox vBox = new VBox();
        Controller.initializeAndShowStage(primaryStage, vBox);
        Controller.setVBox(vBox, 10);
        Controller.addNodes(vBox);
    }

    public static void main(String[] args) {
        launch();
    }
}