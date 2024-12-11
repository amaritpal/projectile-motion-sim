package utils;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public class ButtonStyling {

    // Method to apply button styles
    public static void applyButtonStyles(Button button) {
        button.setStyle(ThemeManager.getCurrentTheme());
    }
}
