package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Credits {

    private Stage primaryStage;

    public Credits(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createCreditsScene() {
        Label creditsLabel = new Label("Projectile Motion Simulator");
        creditsLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-padding: 20px;");

        Label developerLabel = new Label("Developed by: Amarit Pal Singh");
        developerLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        // Create a Back button
        Button backButton = new Button("Back");
        ButtonStyling.applyButtonStyles(backButton); // Apply the ButtonStyling

        backButton.setOnMouseClicked(e -> goBackToMainMenu());

        VBox layout = new VBox(20, creditsLabel, developerLabel, backButton);
        layout.setStyle("-fx-background-color: #2c3e50;");
        layout.setAlignment(javafx.geometry.Pos.CENTER);

        return new Scene(layout, 1280, 720);
    }

    private void goBackToMainMenu() {
        MainMenu mainMenu = new MainMenu(primaryStage);
        Scene mainMenuScene = mainMenu.createMainMenuScene();
        primaryStage.setScene(mainMenuScene);
    }
}
