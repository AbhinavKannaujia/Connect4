package com.abhi.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootgridpane = loader.load();

        controller = loader.getController();
        controller.createplaygroumnd();

        MenuBar menubar = createMenu();

        Pane menuPane = (Pane) rootgridpane.getChildren().get(0);
        menuPane.getChildren().add(menubar);
        menubar.prefWidthProperty().bind(primaryStage.widthProperty());

        Scene scene = new Scene(rootgridpane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect FOUR");
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    private MenuBar createMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newgame = new MenuItem("New Game");
        newgame.setOnAction(event -> controller.resetgame());

        MenuItem resetgame = new MenuItem("Reset Game");
        resetgame.setOnAction(event -> controller.resetgame());

        SeparatorMenuItem sep = new SeparatorMenuItem();

        MenuItem exitgame = new MenuItem("Exit Game");
        exitgame.setOnAction(event -> exitgame());

        fileMenu.getItems().addAll(newgame, resetgame, sep, exitgame);

        Menu helpMenu = new Menu("Help");

        MenuItem aboutGame = new MenuItem("About Connect4");
        aboutGame.setOnAction(event -> aboutconnect4());

        SeparatorMenuItem sep1 = new SeparatorMenuItem();

        MenuItem about_me = new MenuItem("About Me");
        about_me.setOnAction(event -> about_me());

        helpMenu.getItems().addAll(aboutGame, sep1, about_me);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }

    private void about_me() {
        Alert aboutcon = new Alert(Alert.AlertType.INFORMATION);
        aboutcon.setTitle("About the Developer");
        aboutcon.setHeaderText("Abhinav Kannaujia");
        aboutcon.setContentText("ha maine hi banaya hai!!");
        aboutcon.show();
    }

    private void aboutconnect4() {
        Alert aboutconn = new Alert(Alert.AlertType.INFORMATION);
        aboutconn.setTitle("About the Game");
        aboutconn.setHeaderText("How to play?");
        aboutconn.setContentText("Connect Four is a two-player connection game in which the players first choose " +
                "a color and then take turns dropping colored discs from the top into a seven-column, " +
                "six-row vertically suspended grid. The pieces fall straight down, occupying the next available " +
                "space within the column. The objective of the game is to be the first to form a horizontal, vertical," +
                " or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can " +
                "always win by playing the right moves.");
        aboutconn.show();
    }

    private void exitgame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetgame() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
