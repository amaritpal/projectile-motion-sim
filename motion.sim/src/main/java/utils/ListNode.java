package utils;

import javafx.scene.Scene;

public class ListNode {
    Scene scene;
    ListNode next;

    // Constructor
    ListNode(Scene scene) {
        this.scene = scene;
        this.next = null;  // Default next is null
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
