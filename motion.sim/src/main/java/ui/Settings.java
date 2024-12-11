package ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.ButtonStyling;
import utils.SceneManager;
import utils.ThemeManager;

public class Settings {
    private final Stage primaryStage;

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
            volumeLevelLabel.setText("Volume: " + newValue.intValue());
        });

// FPS Toggle Button
        Button fpsToggleButton = new Button("FPS: 30");
        ButtonStyling.applyButtonStyles(fpsToggleButton);
        final boolean[] isFps60 = {false}; // Array to allow modification in lambda
        fpsToggleButton.setOnAction(e -> {
            isFps60[0] = !isFps60[0];
            if (isFps60[0]) {
                fpsToggleButton.setText("FPS: 60");
                // Logic to set FPS to 60
            } else {
                fpsToggleButton.setText("FPS: 30");
                // Logic to set FPS to 30
            }
        });

        // Theme Toggle Button
        Button themeToggleButton = new Button("Theme: Default");
        ButtonStyling.applyButtonStyles(themeToggleButton);
        themeToggleButton.setOnAction(e -> {
            // Cycle through themes: Default -> Dark Mode -> Colorblind
            String currentTheme = ThemeManager.getCurrentTheme();
            switch (currentTheme) {
                case "default" -> ThemeManager.setTheme("darkmode");
                case "darkmode" -> ThemeManager.setTheme("colorblind");
                default -> ThemeManager.setTheme("default");
            }
            themeToggleButton.setText("Theme: " + capitalize(ThemeManager.getCurrentTheme()));
            ThemeManager.applyTheme(primaryStage.getScene());
            System.out.println("Theme changed to: " + ThemeManager.getCurrentTheme());
        });

        // Create Apply button
        Button applyButton = new Button("Apply Settings");
        ButtonStyling.applyButtonStyles(applyButton);
        applyButton.setOnAction(e -> {
            // Logic to apply settings such as volume and FPS change
            System.out.println("Settings Applied: Volume - " + volumeSlider.getValue() + ", FPS - " + fpsToggleButton.getText());
        });

        // Create labels for sliders and toggles
        Label volumeLabel = new Label("Volume:");
        volumeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 10px;");

        // Create HBox for Volume Slider and FPS Toggle Button to appear on the same row
        HBox settingsRow = new HBox(10, volumeLabel, volumeSlider, volumeLevelLabel, fpsToggleButton);
        settingsRow.getStyleClass().add("hbox");
        settingsRow.setAlignment(javafx.geometry.Pos.CENTER);

        // Create a layout with VBox for the entire settings scene
        VBox settingsLayout = new VBox(20, settingsRow, themeToggleButton, applyButton, backButton);
        settingsLayout.getStyleClass().add("vbox");
        settingsLayout.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(settingsLayout, 1280, 720);
        SceneManager.registerScene(scene);
        ThemeManager.applyTheme(scene);
        return scene;
    }

    private void goBackToMainMenu() {
        MainMenu mainMenu = new MainMenu(primaryStage);
        Scene mainMenuScene = mainMenu.createMainMenuScene();
        primaryStage.setScene(mainMenuScene);
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return "";
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
