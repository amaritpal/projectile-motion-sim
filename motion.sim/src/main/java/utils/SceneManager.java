package utils;

import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;

public class SceneManager {
    private static final List<Scene> activeScenes = new ArrayList<>();

    public static void registerScene(Scene scene) {
        activeScenes.add(scene);
    }

    public static void applyCurrentThemeToAll() {
        for (Scene scene : activeScenes) {
            ThemeManager.applyTheme(scene);
        }
    }
}
