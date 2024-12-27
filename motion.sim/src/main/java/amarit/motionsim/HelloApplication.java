
package amarit.motionsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainMenu;
import utils.SettingsManager;
import utils.ThemeManager;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Create the main menu and pass the shared Settings instance
        MainMenu mainMenu = new MainMenu(primaryStage);
        Scene mainMenuScene = mainMenu.createMainMenuScene();

        // Configure the primary stage
        primaryStage.setScene(mainMenuScene);
        SettingsManager.setFPS(60);
        SettingsManager.setTheme("darkmode");
        primaryStage.setResizable(false);
        primaryStage.setTitle("Projectile Motion Simulator");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
