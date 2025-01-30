package ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.ButtonStyling;
import utils.SceneManager;
import utils.ThemeManager;
import utils.SettingsManager;

public class Settings {
    private final Stage primaryStage;

    // Constructor to initialize the primary stage
    public Settings(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createSettingsScene() {
        Button backButton = new Button("Back");
        ButtonStyling.applyButtonStyles(backButton);
        backButton.setOnMouseClicked(e -> goBackToMainMenu());

        Slider volumeSlider = new Slider(0, 100, SettingsManager.getVolume());
        volumeSlider.setBlockIncrement(1);
        volumeSlider.getStyleClass().add("slider");
        volumeSlider.setMaxWidth(200);

        Label volumeLevelLabel = new Label("Volume: " + SettingsManager.getVolume());
        volumeLevelLabel.getStyleClass().add("label");

        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            volumeLevelLabel.setText("Volume: " + newValue.intValue());
            SettingsManager.setVolume((double) newValue.intValue() /100);
        });

        // FPS Toggle Button
        Button fpsToggleButton = new Button("FPS: " + SettingsManager.getFPS());
        ButtonStyling.applyButtonStyles(fpsToggleButton);
        fpsToggleButton.setOnAction(e -> toggleFPS(fpsToggleButton));

        // Theme Toggle Button
        Button themeToggleButton = new Button("Theme: " + capitalize(SettingsManager.getTheme()));
        ButtonStyling.applyButtonStyles(themeToggleButton);
        themeToggleButton.setOnAction(e -> {
            cycleTheme();
            themeToggleButton.setText("Theme: " + capitalize(SettingsManager.getTheme()));
            SceneManager.applyCurrentThemeToAll();
        });

        // Apply button
        Button applyButton = new Button("Apply Settings");
        ButtonStyling.applyButtonStyles(applyButton);
        applyButton.setOnAction(e -> {
            System.out.println("Settings Applied: Volume - " + volumeSlider.getValue() + ", FPS - " + SettingsManager.getFPS());
            SettingsManager.setVolume(volumeSlider.getValue());
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
        SceneManager.removeScene(primaryStage.getScene());
        MainMenu mainMenu = new MainMenu(primaryStage);
        primaryStage.setScene(mainMenu.createMainMenuScene());
    }

    // Toggle the FPS between 30 and 60
    private void toggleFPS(Button button) {
        int currentFPS = SettingsManager.getFPS();
        if (currentFPS == 30) {
            SettingsManager.setFPS(60);
            button.setText("FPS: 60");
        } else {
            SettingsManager.setFPS(30);
            button.setText("FPS: 30");
        }
    }

    // Cycle through themes
    private void cycleTheme() {
        String currentTheme = SettingsManager.getTheme();
        switch (currentTheme) {
            case "default" -> SettingsManager.setTheme("darkmode");
            case "darkmode" -> SettingsManager.setTheme("colorblind");
            default -> SettingsManager.setTheme("default");
        }
    }

    // Capitalize the first letter of the string
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return "";
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
