package ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Simulation {

    private final Stage primaryStage;
    private boolean isRunning = false; // To track the simulation state

    public Simulation(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createSimulationScene() {
        // Visualiser
        Label visualiserLabel = new Label("Visualiser / Graph");
        visualiserLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        VBox visualiserBox = new VBox(visualiserLabel);
        visualiserBox.setStyle("-fx-background-color: #2c3e50; -fx-border-color: white; -fx-border-width: 2px;");
        visualiserBox.setPrefSize(800, 400);

        // Input Values
        Slider velocitySlider = createSlider(0, 100, 50, "Velocity");
        TextField velocityTextField = createTextField("50");
        velocitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            velocityTextField.setText(String.valueOf(newValue.intValue()));
        });

        Slider angleSlider = createSlider(0, 90, 45, "Launch Angle");
        TextField angleTextField = createTextField("45");
        angleSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            angleTextField.setText(String.valueOf(newValue.intValue()));
        });

        ComboBox<String> gravityDropdown = new ComboBox<>();
        gravityDropdown.getItems().addAll("Earth", "Mars", "Moon", "Custom");
        gravityDropdown.setValue("Earth");
        TextField customGravityField = createTextField("");
        customGravityField.setVisible(false);

        gravityDropdown.setOnAction(e -> {
            customGravityField.setVisible(gravityDropdown.getValue().equals("Custom"));
        });

        VBox inputPanel = new VBox(10,
                createLabeledInput("Initial Velocity", velocitySlider, velocityTextField),
                createLabeledInput("Launch Angle", angleSlider, angleTextField),
                createLabeledInput("Gravity (Planet)", gravityDropdown, customGravityField)
        );
        inputPanel.setStyle("-fx-background-color: #34495e; -fx-padding: 10px;");

        // Control Buttons
        Button launchButton = createStyledButton("Launch");
        Button playButton = createStyledButton("Play");
        Button pauseButton = createStyledButton("Pause");
        Button resetButton = createStyledButton("Reset");

        // Launch Button Logic
        launchButton.setOnAction(e -> {
            if (!isRunning) {
                isRunning = true;
                double velocity = velocitySlider.getValue();
                double angle = angleSlider.getValue();
                double gravity;

                if (gravityDropdown.getValue().equals("Custom")) {
                    try {
                        gravity = Double.parseDouble(customGravityField.getText());
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid gravity value. Defaulting to 9.8 m/s²");
                        gravity = 9.8;
                    }
                } else {
                    gravity = getGravityForPlanet(gravityDropdown.getValue());
                }

                System.out.println("Simulation started with:");
                System.out.println("Velocity: " + velocity + " m/s");
                System.out.println("Angle: " + angle + " degrees");
                System.out.println("Gravity: " + gravity + " m/s²");

                startSimulation(velocity, angle, gravity);
            }
        });

        // Play Button Logic
        playButton.setOnAction(e -> {
            if (!isRunning) {
                isRunning = true;
                System.out.println("Simulation resumed.");
                // Add logic to resume the simulation here
            }
        });

        // Pause Button Logic
        pauseButton.setOnAction(e -> {
            if (isRunning) {
                isRunning = false;
                System.out.println("Simulation paused.");
                // Add logic to pause the simulation here
            }
        });

        // Reset Button Logic
        resetButton.setOnAction(e -> resetSimulation(velocitySlider, angleSlider, gravityDropdown, customGravityField));

        // Control Panel Layout
        HBox controlPanel = new HBox(10, launchButton, playButton, pauseButton, resetButton);
        controlPanel.setAlignment(javafx.geometry.Pos.CENTER);
        controlPanel.setStyle("-fx-padding: 10px; -fx-background-color: #34495e;");

        // Statistics
        Label heightLabel = createStyledLabel("Height: 0.0 m");
        Label velocityLabel = createStyledLabel("Velocity: 0.0 m/s");
        Label timeLabel = createStyledLabel("Time: 0.0 s");

        VBox statisticsPanel = new VBox(10, heightLabel, velocityLabel, timeLabel);
        statisticsPanel.setStyle("-fx-background-color: #34495e; -fx-padding: 10px;");

        // Layout Setup
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #2c3e50;");
        mainLayout.setTop(statisticsPanel);
        mainLayout.setLeft(inputPanel);
        mainLayout.setCenter(visualiserBox);

        VBox bottomLayout = new VBox(10, controlPanel, createBackButton());
        bottomLayout.setAlignment(javafx.geometry.Pos.CENTER);

        mainLayout.setBottom(bottomLayout);
        return new Scene(mainLayout, 1280, 720);

    }

    // Helper Methods
    private Slider createSlider(double min, double max, double value, String label) {
        Slider slider = new Slider(min, max, value);
        slider.setStyle("-fx-accent: #2980b9;");
        slider.setMaxWidth(200);
        return slider;
    }

    private TextField createTextField(String text) {
        TextField textField = new TextField(text);
        textField.setMaxWidth(100);
        return textField;
    }

    private HBox createLabeledInput(String labelText, Control input, Control additionalInput) {
        Label label = createStyledLabel(labelText);
        HBox inputRow = new HBox(10, label, input, additionalInput);
        inputRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return inputRow;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #193249; -fx-text-fill: white;");
        button.setMinWidth(200);

        // Hover effect for the button
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #2980b9; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #193249; -fx-text-fill: white;"));

        return button;
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        return label;
    }

    private VBox createBackButton() {
        Button backButton = createStyledButton("Back");
        backButton.setOnAction(e -> goBackToMainMenu());
        VBox backButtonBox = new VBox(backButton);
        backButtonBox.setAlignment(javafx.geometry.Pos.CENTER);
        return backButtonBox;
    }

    private void startSimulation(double velocity, double angle, double gravity) {
        //simulation logic to be added
        System.out.println("Simulation logic running...");
    }

    private void goBackToMainMenu() {
        MainMenu mainMenu = new MainMenu(primaryStage);
        primaryStage.setScene(mainMenu.createMainMenuScene());
    }

    private double getGravityForPlanet(String planet) {
        switch (planet) {
            case "Earth":
                return 9.8;
            case "Moon":
                return 1.6;
            case "Mars":
                return 3.7;
            default:
                return 9.8; // Default to Earth's gravity
        }
    }

    private void resetSimulation(Slider velocitySlider, Slider angleSlider, ComboBox<String> gravityDropdown, TextField customGravityField) {
        // Reset sliders to default values
        velocitySlider.setValue(50); // Default velocity
        angleSlider.setValue(45); // Default angle
        gravityDropdown.setValue("Earth"); // Default gravity
        customGravityField.clear(); // Clear custom gravity input field
        System.out.println("Simulation reset to default values!");
    }
}
