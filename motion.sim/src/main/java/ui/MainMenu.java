package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    private Stage primaryStage;

    // Constructor
    public MainMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Create the Main Menu scene
    public Scene createMainMenuScene() {
        // Create the title label
        Label titleLabel = new Label("Projectile Motion Simulator");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: white; -fx-padding: 20px;");

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
        mainMenuLayout.setStyle("-fx-background-color: #2c3e50;");
        mainMenuLayout.setPrefSize(400, 300);
        mainMenuLayout.setAlignment(javafx.geometry.Pos.CENTER);

        return new Scene(mainMenuLayout, 1280, 720);
    }

    private void startSimulation() {
        // Switch to the simulation scene here
        System.out.println("Starting Simulation...");
        Simulation simulation = new Simulation(primaryStage); // Pass primaryStage to Simulation
        Scene simulationScene = simulation.createSimulationScene();
        primaryStage.setScene(simulationScene); // Set the simulation scene
    }

    private void openSettings() {
        // Switch to the settings scene here
        System.out.println("Opening Settings...");
        Settings settings = new Settings(primaryStage); // Pass primaryStage to Settings
        Scene settingsScene = settings.createSettingsScene();
        primaryStage.setScene(settingsScene); // Set the settings scene
    }

    private void showCredits() {
        // Switch to the credits scene here
        System.out.println("Showing Credits...");
        Credits credits = new Credits(primaryStage); // Pass primaryStage to Credits
        Scene creditsScene = credits.createCreditsScene();
        primaryStage.setScene(creditsScene); // Set the credits scene
    }

    private void exitApplication() {
        System.out.println("Navigating to Exit Confirmation...");
        Exit exitConfirmation = new Exit(primaryStage); // Create ExitConfirmation instance
        Scene exitScene = exitConfirmation.createExitScene();
        primaryStage.setScene(exitScene);
    }

    // Helper method to create a styled button
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #34495e; -fx-text-fill: white;");
        button.setMinWidth(200);

        // Hover effect for the button
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #2980b9; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #34495e; -fx-text-fill: white;"));

        return button;
    }

}
