package utils;

import javafx.scene.Scene;

/**
 * This class manages scenes in the application, allowing theme application across all scenes.
 */
public class SceneManager {
    public static final LinkedList scenes = new LinkedList();
    static int noOfScenes = 0;

    /**
     * Registers a new scene with the SceneManager.
     *
     * @param scene The scene to be registered.
     */
    public static void registerScene(Scene scene) {
        scenes.insert(scene);
        noOfScenes++;
    }

    /**
     * Unregisters a scene from the SceneManager.
     *
     * @param scene The scene to be unregistered.
     */
    public static void removeScene(Scene scene) {
        scenes.remove(scene);
        noOfScenes--;
    }

    /**
     * Applies the current theme to all registered scenes.
     * The theme is applied to each scene by calling the applyTheme method of the ThemeManager.
     */
    public static void applyCurrentThemeToAll() {
        ListNode currentNode = scenes.getHeadNode();  // Get the head node of the list

        while (currentNode != null) {
            ThemeManager.applyTheme(currentNode.getScene());  // Apply the theme to each scene
            currentNode = currentNode.next;  // Move to the next node in the list
        }
    }
}