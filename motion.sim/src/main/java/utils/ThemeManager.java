package utils;

import javafx.scene.Scene;
import java.net.URL;

public class ThemeManager {

    private static String currentTheme = "default";

    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        String themePath = switch (currentTheme) {
            case "darkmode" -> "/styles/darkmode.css";
            case "colorblind" -> "/styles/colorblind.css";
            default -> "/styles/default.css";
        };
        URL themeUrl = ThemeManager.class.getResource(themePath);
        if (themeUrl == null) {
            System.err.println("Theme resource not found: " + themePath);
            return;
        }
        System.out.println("Applying theme: " + themeUrl.toExternalForm());
        scene.getStylesheets().add(themeUrl.toExternalForm());
    }

    public static void setTheme(String theme) {
        currentTheme = theme;
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }
}
