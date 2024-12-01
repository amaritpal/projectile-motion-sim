package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Exit {

    private Stage primaryStage;

    public Exit(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createExitScene() {
        // Create a label asking for confirmation
        Label confirmationLabel = new Label("Are you sure you want to quit?");
        confirmationLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-padding: 20px;");

        // Create Yes button
        Button yesButton = new Button("Yes");
        yesButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #e74c3c; -fx-text-fill: white;");
        yesButton.setOnMouseEntered(e -> yesButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #c0392b; -fx-text-fill: white;"));
        yesButton.setOnMouseExited(e -> yesButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #e74c3c; -fx-text-fill: white;"));
        yesButton.setOnAction(e -> exitApplication());

        // Create No button
        Button noButton = new Button("No");
        noButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #2980b9; -fx-text-fill: white;");
        noButton.setOnMouseEntered(e -> noButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #1c638a; -fx-text-fill: white;"));
        noButton.setOnMouseExited(e -> noButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #2980b9; -fx-text-fill: white;"));
        noButton.setOnAction(e -> goBackToMainMenu());

        // Create a layout and add elements
        VBox exitLayout = new VBox(20, confirmationLabel, yesButton, noButton);
        exitLayout.setStyle("-fx-background-color: #2c3e50;");
        exitLayout.setAlignment(javafx.geometry.Pos.CENTER);

        // Return the scene
        return new Scene(exitLayout, 1280, 720);
    }

    private void exitApplication() {
        System.out.println("Exiting Application...");
        System.exit(0); // Exit the application
    }

    private void goBackToMainMenu() {
        MainMenu mainMenu = new MainMenu(primaryStage);
        Scene mainMenuScene = mainMenu.createMainMenuScene();
        primaryStage.setScene(mainMenuScene);
    }
}
