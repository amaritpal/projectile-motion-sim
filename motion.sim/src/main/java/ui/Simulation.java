package ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Simulation {

    private Stage primaryStage;
    private double gravity = 9.81; // Default gravity (Earth)
    private boolean isRunning = false;
    private boolean isPaused = false;
    public Simulation(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createSimulationScene() {
        // Viewing Box
        Label viewingBoxLabel = new Label("Viewing Box");
        viewingBoxLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-padding: 10px; -fx-border-color: white; -fx-border-width: 2px;");
        VBox viewingBox = new VBox(viewingBoxLabel);
        viewingBox.setStyle("-fx-background-color: #2c3e50;");
        viewingBox.setPrefSize(800, 400); // Adjust size based on mock-up

        // Variable Adjuster (Sliders)
        Slider angleSlider = new Slider(0, 90, 45); // Angle range: 0° to 90°
        Label angleLabel = new Label("Angle: 45°");
        angleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            angleLabel.setText("Angle: " + newVal.intValue() + "°");
        });

        Slider massSlider = new Slider(0.1, 10, 1); // Mass range: 0.1kg to 10kg
        Label massLabel = new Label("Mass: 1.0 kg");
        massLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        massSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            massLabel.setText(String.format("Mass: %.1f kg", newVal.doubleValue()));
        });

        Slider velocitySlider = new Slider(0, 100, 50); // Velocity range: 0 m/s to 100 m/s
        Label velocityLabel = new Label("Velocity: 50.0 m/s");
        velocityLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        velocitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            velocityLabel.setText(String.format("Velocity: %.1f m/s", newVal.doubleValue()));
        });

        VBox variableAdjuster = new VBox(10, angleLabel, angleSlider, massLabel, massSlider, velocityLabel, velocitySlider);
        variableAdjuster.setStyle("-fx-background-color: #34495e; -fx-padding: 10px;");
        variableAdjuster.setPrefWidth(300);

        // Gravity Toggle (Preset) + Custom Gravity Input
        ToggleButton marsButton = new ToggleButton("Mars");
        ToggleButton moonButton = new ToggleButton("Moon");
        ToggleButton earthButton = new ToggleButton("Earth");
        ToggleButton customButton = new ToggleButton("Custom");
        earthButton.setSelected(true); // Default selection

        // Custom Gravity Input
        Label customGravityLabel = new Label("Custom Gravity:");
        customGravityLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        TextField customGravityInput = new TextField();
        customGravityInput.setPromptText("Enter gravity value...");
        customGravityInput.setStyle("-fx-font-size: 16px; -fx-padding: 5px;");
        customGravityInput.setDisable(true); // Disable until Custom is selected

        customGravityInput.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                gravity = Double.parseDouble(newVal);
            } catch (NumberFormatException e) {
                gravity = 9.81; // Default to Earth gravity if input is invalid
            }
        });

        // Update gravity when preset buttons are clicked
        marsButton.setOnAction(e -> updateGravity(3.71, marsButton, moonButton, earthButton, customButton));
        moonButton.setOnAction(e -> updateGravity(1.62, marsButton, moonButton, earthButton, customButton));
        earthButton.setOnAction(e -> updateGravity(9.81, marsButton, moonButton, earthButton, customButton));
        customButton.setOnAction(e -> {
            if (customButton.isSelected()) {
                customGravityInput.setDisable(false); // Enable the custom input
            } else {
                customGravityInput.setDisable(true); // Disable the custom input
            }
        });

        // Gravity Selector HBox
        HBox gravitySelector = new HBox(10, marsButton, moonButton, earthButton, customButton);
        gravitySelector.setStyle("-fx-background-color: #34495e; -fx-padding: 10px;");
        gravitySelector.setAlignment(javafx.geometry.Pos.CENTER);

        HBox customGravityLayout = new HBox(10, customGravityLabel, customGravityInput);
        customGravityLayout.setStyle("-fx-background-color: #34495e; -fx-padding: 10px;");
        customGravityLayout.setAlignment(javafx.geometry.Pos.CENTER);

        // Statistics Panel (Moved to top-right)
        Label maxHeightLabel = new Label("Max Height: 0.0 m");
        Label timeLabel = new Label("Time: 0.0 s");
        Label velocityStatLabel = new Label("Velocity: 0.0 m/s");
        maxHeightLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        timeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        velocityStatLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        VBox statisticsPanel = new VBox(10, maxHeightLabel, timeLabel, velocityStatLabel);
        statisticsPanel.setStyle("-fx-background-color: #34495e; -fx-padding: 10px;");
        statisticsPanel.setPrefWidth(300);

        // Control Buttons (VBox on the right side for Launch, Pause, and Play)
        Button launchButton = new Button("Launch");
        launchButton.setOnAction(e -> startSimulation(angleSlider, massSlider, velocitySlider));
        ButtonStyling.applyButtonStyles(launchButton);

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> pauseSimulation());
        ButtonStyling.applyButtonStyles(pauseButton);

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> resumeSimulation());
        ButtonStyling.applyButtonStyles(playButton);

        VBox simulationControls = new VBox(10, launchButton, pauseButton, playButton);
        simulationControls.setStyle("-fx-background-color: #34495e; -fx-padding: 10px;");
        simulationControls.setAlignment(javafx.geometry.Pos.CENTER);

        // Bottom control buttons (Back and Reset)
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> goBackToMainMenu());
        ButtonStyling.applyButtonStyles(backButton);

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetSimulation(angleSlider, massSlider, velocitySlider));
        ButtonStyling.applyButtonStyles(resetButton);

        HBox controlButtons = new HBox(10, backButton, resetButton);
        controlButtons.setAlignment(javafx.geometry.Pos.CENTER);

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #2c3e50;");
        mainLayout.setTop(viewingBox);
        mainLayout.setLeft(variableAdjuster);
        mainLayout.setRight(simulationControls); // Place the controls in the right side
        mainLayout.setCenter(gravitySelector); // Place gravity selector in the center
        mainLayout.setBottom(controlButtons);
        mainLayout.setTop(statisticsPanel); // Move statistics to the top-right
        mainLayout.setBottom(customGravityLayout); // Place custom gravity input at the bottom

        return new Scene(mainLayout, 1280, 720);
    }

    private void updateGravity(double gravityValue, ToggleButton mars, ToggleButton moon, ToggleButton earth, ToggleButton custom) {
        gravity = gravityValue;
        mars.setSelected(gravity == 3.71);
        moon.setSelected(gravity == 1.62);
        earth.setSelected(gravity == 9.81);
        custom.setSelected(false); // Deselect Custom

        System.out.println("Gravity set to " + gravity);
        // Logic to update gravity in the simulation
    }

    private void startSimulation(Slider angleSlider, Slider massSlider, Slider velocitySlider) {
        if (!isRunning) {
            // Begin simulation logic here using the sliders' values
            System.out.println("Simulation Started");
            isRunning = true;
            isPaused = false;
            // Logic to launch the simulation with the current values
        }
    }

    private void pauseSimulation() {
        if (isRunning) {
            System.out.println("Simulation Paused");
            isPaused = true;
            isRunning = false;
        }
    }

    private void resumeSimulation() {
        if (!isRunning && isPaused) {
            System.out.println("Simulation Resumed");
            isRunning = true;
            isPaused = false;
        }
    }

    private void resetSimulation(Slider angleSlider, Slider massSlider, Slider velocitySlider) {
        angleSlider.setValue(45); // Default angle
        massSlider.setValue(1); // Default mass
        velocitySlider.setValue(50); // Default velocity
        System.out.println("Simulation Reset to Defaults");
        // Additional logic to reset the simulation state
    }

    private void goBackToMainMenu() {
        MainMenu mainMenu = new MainMenu(primaryStage);
        Scene mainMenuScene = mainMenu.createMainMenuScene();
        primaryStage.setScene(mainMenuScene); // Set the main menu scene
    }
}
