package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Settings {
    private Stage primaryStage;

    public Settings(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createSettingsScene() {
        // Create a Back button
        Button backButton = new Button("Back");
        ButtonStyling.applyButtonStyles(backButton); // Apply the ButtonStyling

        backButton.setOnMouseClicked(e -> goBackToMainMenu());

        // Volume Slider
        Slider volumeSlider = new Slider(0, 100, 50); // Min 0, Max 100, Default 50
        volumeSlider.setBlockIncrement(1);
        volumeSlider.setStyle("-fx-accent: #2980b9;"); // Slider color
        volumeSlider.setMaxWidth(200); // Limit width of volume slider

        // Label for the volume level
        Label volumeLevelLabel = new Label("Volume: 50");
        volumeLevelLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 10px;");

        // Update volume label as slider value changes
        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            volumeLevelLabel.setText("Volume: " + (int) newValue.intValue());
        });

        // FPS Toggle Button
        ToggleButton fpsToggleButton = new ToggleButton("FPS: 30");
        fpsToggleButton.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 10px; " +
                "-fx-background-color: #34495e; -fx-border-color: white; -fx-border-width: 2px;");
        fpsToggleButton.setOnMouseEntered(e -> fpsToggleButton.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 10px; -fx-background-color: #2980b9; -fx-border-color: white; -fx-border-width: 2px;"));
        fpsToggleButton.setOnMouseExited(e -> fpsToggleButton.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 10px; -fx-background-color: #34495e; -fx-border-color: white; -fx-border-width: 2px;"));
        fpsToggleButton.setOnAction(e -> {
            if (fpsToggleButton.isSelected()) {
                fpsToggleButton.setText("FPS: 60");
                // Logic to set FPS to 60
            } else {
                fpsToggleButton.setText("FPS: 30");
                // Logic to set FPS to 30
            }
        });

        // Create Apply button
        Button applyButton = new Button("Apply Settings");
        ButtonStyling.applyButtonStyles(applyButton); // Apply the ButtonStyling
        applyButton.setOnAction(e -> {
            // Logic to apply settings such as volume and FPS change
            System.out.println("Settings Applied: Volume - " + volumeSlider.getValue() + ", FPS - " + fpsToggleButton.getText());
        });

        // Create labels for sliders and toggles
        Label volumeLabel = new Label("Volume:");
        volumeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 10px;");

        // Create HBox for Volume Slider and FPS Toggle Button to appear on the same row
        HBox settingsRow = new HBox(10, volumeLabel, volumeSlider, volumeLevelLabel, fpsToggleButton);
        settingsRow.setStyle("-fx-background-color: #34495e;");
        settingsRow.setAlignment(javafx.geometry.Pos.CENTER);

        // Create a layout with VBox for the entire settings scene
        VBox settingsLayout = new VBox(20, settingsRow, applyButton, backButton);
        settingsLayout.setStyle("-fx-background-color: #34495e;");
        settingsLayout.setAlignment(javafx.geometry.Pos.CENTER);

        // Return the Scene
        return new Scene(settingsLayout, 1280, 720);
    }

    private void goBackToMainMenu() {
        MainMenu mainMenu = new MainMenu(primaryStage);  // Pass primaryStage to MainMenu
        Scene mainMenuScene = mainMenu.createMainMenuScene();
        primaryStage.setScene(mainMenuScene); // Set the main menu scene
    }
}
