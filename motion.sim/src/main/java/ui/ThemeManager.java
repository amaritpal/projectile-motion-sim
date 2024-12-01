package ui;

import javafx.scene.Scene;

public class ThemeManager {

    private static String currentTheme = "default";

    public static void applyTheme(Scene scene) {
        switch (currentTheme) {
            case "darkmode":
                scene.getStylesheets().clear();
                scene.getStylesheets().add(ThemeManager.class.getResource("/darkmode.css").toExternalForm());
                break;
            case "colorblind":
                scene.getStylesheets().clear();
                scene.getStylesheets().add(ThemeManager.class.getResource("/colorblind.css").toExternalForm());
                break;
            default:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(ThemeManager.class.getResource("/default.css").toExternalForm());
                break;
        }
    }

    public static void setTheme(String theme) {
        currentTheme = theme;
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }
}
