package utils;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import utils.ThemeManager;

public class ButtonStyling {

    // Method to apply button styles and hover effects
    public static void applyButtonStyles(Button button) {
        button.setStyle(getDefaultStyle());

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle(getHoverStyle()));
        button.setOnMouseExited(e -> button.setStyle(getDefaultStyle()));
    }

    // Get the default style based on the current theme
    private static String getDefaultStyle() {
        switch (ThemeManager.getCurrentTheme()) {
            case "darkmode":
                return "-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                        "-fx-background-color: #2c3e50; -fx-text-fill: white; " +
                        "-fx-border-color: white; -fx-border-width: 2px;";
            case "colorblind":
                return "-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                        "-fx-background-color: #7f8c8d; -fx-text-fill: black; " +
                        "-fx-border-color: black; -fx-border-width: 2px;";
            default: // Default theme
                return "-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                        "-fx-background-color: #34495e; -fx-text-fill: white; " +
                        "-fx-border-color: white; -fx-border-width: 2px;";
        }
    }

    // Get the hover style based on the current theme
    private static String getHoverStyle() {
        switch (ThemeManager.getCurrentTheme()) {
            case "darkmode":
                return "-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                        "-fx-background-color: #1abc9c; -fx-text-fill: white; " +
                        "-fx-border-color: white; -fx-border-width: 2px;";
            case "colorblind":
                return "-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                        "-fx-background-color: #bdc3c7; -fx-text-fill: black; " +
                        "-fx-border-color: black; -fx-border-width: 2px;";
            default: // Default theme
                return "-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                        "-fx-background-color: #4277ab; -fx-text-fill: white; " +
                        "-fx-border-color: white; -fx-border-width: 2px;";
        }
    }

    public static void applyButtonStyles(ToggleButton toggle) {
        toggle.setStyle(getDefaultStyle());

        // Hover effect
        toggle.setOnMouseEntered(e -> toggle.setStyle(getHoverStyle()));
        toggle.setOnMouseExited(e -> toggle.setStyle(getDefaultStyle()));
    }
}
