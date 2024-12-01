package ui;

import javafx.scene.control.Button;

public class ButtonStyling {

    // Method to apply button styles and hover effects
    public static void applyButtonStyles(Button button) {
        button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                "-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-border-color: white; -fx-border-width: 2px;");

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                "-fx-background-color: #4277ab; -fx-text-fill: white; " +
                "-fx-border-color: white; -fx-border-width: 2px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                "-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-border-color: white; -fx-border-width: 2px;"));
    }
}
