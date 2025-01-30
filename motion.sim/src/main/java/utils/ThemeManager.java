package utils;

import javafx.scene.Scene;
import java.net.URL;

public class ThemeManager {

    private static String currentTheme = "default";  // The default theme is set to "default"

    // Applies the current theme to the given sscene by updating its stylesheet
    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();  // Clear any existing stylesheets
        // Determine the path to the CSS file based on the current theme
        String themePath = switch (currentTheme) {
            case "darkmode" -> "/styles/darkmode.css";  // Path for dark mode theme
            case "colorblind" -> "/styles/colorblind.css";  // Path for colorblind theme
            default -> "/styles/default.css";  // Path for the default theme
        };
        // Get the URL of the corresponding theme resource
        URL themeUrl = ThemeManager.class.getResource(themePath);
        if (themeUrl == null) {
            System.err.println("Theme resource not found: " + themePath);  // Error if theme is not found
            return;
        }
        // Add the selected theme's CSS stylesheet to the scene
        scene.getStylesheets().add(themeUrl.toExternalForm());
    }



    // Sets the current theme to the specified theme
    public static void setTheme(String theme) {
        currentTheme = theme;  // Update the current theme
    }

    // Returns the name of the current theme
    public static String getCurrentTheme() {
        return currentTheme;  // Return the current theme
    }
}
