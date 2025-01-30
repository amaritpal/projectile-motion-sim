package amarit.motionsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainMenu;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        MainMenu mainMenu = new MainMenu(primaryStage);
        Scene mainMenuScene = mainMenu.createMainMenuScene();

        // Configure the primary stage
        primaryStage.setScene(mainMenuScene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Projectile Motion Simulator");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
