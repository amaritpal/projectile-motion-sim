package amarit.motionsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainMenu;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create an instance of the MainMenu class
        MainMenu mainMenu = new MainMenu(primaryStage);

        // Set up the scene for the main menu
        Scene mainMenuScene = mainMenu.createMainMenuScene();

        // Set up the stage (window)
        primaryStage.setScene(mainMenuScene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Projectile Motion Simulator");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
