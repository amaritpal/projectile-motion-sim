package utils;

import javafx.scene.Scene;
import java.net.URL;

public class ThemeManager {

    private static String currentTheme = "default";

    // Applies the selected theme to the given scene
    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        String themePath = switch (currentTheme) {
            case "darkmode" -> "/styles/darkmode.css";
            case "colorblind" -> "/styles/colorblind.css";
            default -> "/styles/default.css";
        };

        URL themeUrl = ThemeManager.class.getResource(themePath);
        if (themeUrl == null) {
            throw new IllegalArgumentException("Theme resource not found: " + themePath);
        }

        scene.getStylesheets().add(themeUrl.toExternalForm());
    }

    // Sets the current theme
    public static void setTheme(String theme) {
        currentTheme = theme;
    }

    // Gets the current theme
    public static String getCurrentTheme() {
        return currentTheme;
    }
}
