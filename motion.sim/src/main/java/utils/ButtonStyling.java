package utils;

import javafx.scene.control.Button;

public class ButtonStyling {

    // Method to apply button styles
    public static void applyButtonStyles(Button button) {
        button.setStyle(ThemeManager.getCurrentTheme());
    }
}
