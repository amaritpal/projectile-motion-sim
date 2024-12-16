package utils;

import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages scenes in the application, allowing theme application across all scenes.
 */
public class SceneManager {
    private static final List<Scene> scenes = new ArrayList<>();

    public static void registerScene(Scene scene) {
        scenes.add(scene);
    }

    public static void applyCurrentThemeToAll() {
        for (Scene scene : scenes) {
            ThemeManager.applyTheme(scene);
        }
    }
}
