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
    private Stage primaryStage;
    private int fps;

    // Constructor to initialize the primary stage and default FPS
    public Settings(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.fps = 30; // Default FPS value
    }

    // Method to create and return the settings scene
    public Scene createSettingsScene() {
        Button backButton = new Button("Back");
        ButtonStyling.applyButtonStyles(backButton);
        backButton.setOnMouseClicked(e -> goBackToMainMenu());

        Slider volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setBlockIncrement(1);
        volumeSlider.getStyleClass().add("slider");
        volumeSlider.setMaxWidth(200);

        Label volumeLevelLabel = new Label("Volume: 50");
        volumeLevelLabel.getStyleClass().add("label");

        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) ->
                volumeLevelLabel.setText("Volume: " + newValue.intValue()));

        // FPS Toggle Button
        Button fpsToggleButton = new Button("FPS: 30");
        ButtonStyling.applyButtonStyles(fpsToggleButton);
        fpsToggleButton.setOnAction(e -> toggleFPS(fpsToggleButton));

        // Theme Toggle Button
        Button themeToggleButton = new Button("Theme: Default");
        ButtonStyling.applyButtonStyles(themeToggleButton);
        themeToggleButton.setOnAction(e -> {
            cycleTheme();
            themeToggleButton.setText("Theme: " + capitalize(ThemeManager.getCurrentTheme()));
            SceneManager.applyCurrentThemeToAll();
        });

        // Apply button
        Button applyButton = new Button("Apply Settings");
        ButtonStyling.applyButtonStyles(applyButton);
        applyButton.setOnAction(e -> {
            System.out.println("Settings Applied: Volume - " + volumeSlider.getValue() + ", FPS - " + fps);
        });

        HBox settingsRow = new HBox(10, new Label("Volume:"), volumeSlider, volumeLevelLabel, fpsToggleButton);
        settingsRow.getStyleClass().add("hbox");
        settingsRow.setAlignment(javafx.geometry.Pos.CENTER);

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

    // Toggle the FPS between 30 and 60
    private void toggleFPS(Button button) {
        if (fps == 30) {
            fps = 60;
            button.setText("FPS: 60");
        } else {
            fps = 30;
            button.setText("FPS: 30");
        }
    }

    // Cycle through themes
    private void cycleTheme() {
        String currentTheme = ThemeManager.getCurrentTheme();
        switch (currentTheme) {
            case "default" -> ThemeManager.setTheme("darkmode");
            case "darkmode" -> ThemeManager.setTheme("colorblind");
            default -> ThemeManager.setTheme("default");
        }
    }

    // Capitalize the first letter of the string
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return "";
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    // Getter for FPS
    public int getFPS() {
        return this.fps;
    }

    // Setter for FPS
    public void setFPS(int fps) {
        this.fps = fps;
    }
}
