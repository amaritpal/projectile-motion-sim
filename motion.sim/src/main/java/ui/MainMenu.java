package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.SceneManager;
import utils.ThemeManager;

public class MainMenu {

    private final Stage primaryStage;

    // Constructor
    public MainMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Create the Main Menu scene
    public Scene createMainMenuScene() {
        // Create the title label
        Label titleLabel = new Label("Projectile Motion Simulator");
        titleLabel.getStyleClass().add("label"); // Use the .label style from CSS
        titleLabel.setStyle("-fx-font-size: 36px; -fx-padding: 20px;"); // Additional inline customization

        Button startButton = createStyledButton("Start Simulation");
        Button settingsButton = createStyledButton("Settings");
        Button creditsButton = createStyledButton("Credits");
        Button exitButton = createStyledButton("Exit");

        // Event handlers for buttons
        startButton.setOnAction(e -> startSimulation());
        settingsButton.setOnAction(e -> openSettings());
        creditsButton.setOnAction(e -> showCredits());
        exitButton.setOnAction(e -> exitApplication());

        // Create the main menu layout and add the title and buttons
        VBox mainMenuLayout = new VBox(20, titleLabel, startButton, settingsButton, creditsButton, exitButton);
        mainMenuLayout.getStyleClass().add("vboxPadded"); // Use the .vboxPadded style from CSS
        mainMenuLayout.setPrefSize(400, 300);
        mainMenuLayout.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(mainMenuLayout, 1280, 720);
        ThemeManager.applyTheme(scene);

        return scene;
    }

    private void startSimulation() {
        System.out.println("Starting Simulation...");
        Simulation simulation = new Simulation(primaryStage);
        Scene simulationScene = simulation.createSimulationScene();
        primaryStage.setScene(simulationScene);
    }

    private void openSettings() {
        System.out.println("Opening Settings...");
        Settings settings = new Settings(primaryStage);
        Scene settingsScene = settings.createSettingsScene();
        primaryStage.setScene(settingsScene);
    }

    private void showCredits() {
        System.out.println("Showing Credits...");
        Credits credits = new Credits(primaryStage);
        Scene creditsScene = credits.createCreditsScene();
        primaryStage.setScene(creditsScene);
    }

    private void exitApplication() {
        System.out.println("Navigating to Exit Confirmation...");
        Exit exitConfirmation = new Exit(primaryStage);
        Scene exitScene = exitConfirmation.createExitScene();
        primaryStage.setScene(exitScene);
    }

    // Helper method to create a styled button
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("button"); // Use the .button style from CSS
        button.setMinWidth(200);
        return button;
    }
}
