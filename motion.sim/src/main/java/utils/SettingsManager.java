package utils;

/**
 * This class manages the application's settings such as FPS, volume, and theme.
 */
public class SettingsManager {
    private static int fps = 30;  // Default FPS value
    private static double volume = 50;  // Default volume value
    private static String theme = "default";  // Default theme value

    //Returns the current FPS value.
    public static int getFPS() {
        return fps;
    }

    //Sets the FPS value.
    public static void setFPS(int fps) {
        SettingsManager.fps = fps;
    }

    //Returns the current volume value.
    public static double getVolume() {
        return volume;
    }

    //Sets the volume value.
    public static void setVolume(double volume) {
        SettingsManager.volume = volume;
    }

    //Returns the current theme value.
    public static String getTheme() {
        return theme;
    }

    //Sets the theme value and updates the theme in the ThemeManager.
    public static void setTheme(String theme) {
        SettingsManager.theme = theme;
        ThemeManager.setTheme(theme);
    }
}