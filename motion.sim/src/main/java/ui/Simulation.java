package ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utils.SceneManager;
import utils.ThemeManager;

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
        visualiserBox.getStyleClass().add("visualiser");
        visualiserBox.setPrefSize(800, 400);

        Slider velocitySlider = createSlider(0, 100, 0);
        TextField velocityTextField = createTextField("0");

        // Listener for slider changes
        velocitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            velocityTextField.setText(String.valueOf(newValue.intValue()));
        });

        // Listener for text field changes
        velocityTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                double value = Double.parseDouble(newValue);
                if (value >= 0 && value <= 100) {
                    velocitySlider.setValue(value);
                } else {
                    // If the value is out of range, revert to the slider's current value
                    velocityTextField.setText(String.valueOf((int) velocitySlider.getValue()));
                }
            } catch (NumberFormatException e) {
                // If the input is not a valid number, revert to the slider's current value
                velocityTextField.setText(String.valueOf((int) velocitySlider.getValue()));
            }
        });


        Slider angleSlider = createSlider(0, 180, 0);
        TextField angleTextField = createTextField("0");

        // Listener for angle slider changes
        angleSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            angleTextField.setText(String.valueOf(newValue.intValue()));
        });

        // Listener for angle text field changes
        angleTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                double value = Double.parseDouble(newValue);
                if (value >= 0 && value <= 180) {
                    angleSlider.setValue(value);
                } else {
                    // If the value is out of range, revert to the slider's current value
                    angleTextField.setText(String.valueOf((int) angleSlider.getValue()));
                }
            } catch (NumberFormatException e) {
                // If the input is not a valid number, revert to the slider's current value
                angleTextField.setText(String.valueOf((int) angleSlider.getValue()));
            }
        });

        ComboBox<String> gravityDropdown = new ComboBox<>();
        gravityDropdown.getItems().addAll("Earth", "Mars", "Moon", "Custom");
        gravityDropdown.setValue("Earth");
        TextField customGravityField = createTextField("");
        customGravityField.setVisible(false);

        gravityDropdown.setOnAction(e -> {
            boolean isCustom = gravityDropdown.getValue().equals("Custom");
            customGravityField.setVisible(isCustom);
            if (!isCustom) {
                customGravityField.clear();
            }
        });

// Add a listener to the customGravityField
        customGravityField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    double gravity = Double.parseDouble(newValue);
                    if (gravity <= 0) {
                        showAlert("Invalid Gravity", "Gravity must be positive. Using Earth's gravity (9.8 m/s²).");
                        customGravityField.setText("9.8");
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "Please enter a valid number for gravity. Using Earth's gravity (9.8 m/s²).");
                    customGravityField.setText("9.8");
                }
            }
        });
        TextField massTextField = createTextField("0"); // TextField for mass input

// Listener for mass text field changes
        massTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                double value = Double.parseDouble(newValue);
                if (value >= 0 && value <= 1000) { // You can adjust the range based on your requirements
                    // Valid mass value
                } else {
                    // If the value is out of range, show an error
                    showAlert("Invalid Mass", "Mass must be between 0 and 1000 kg.");
                    massTextField.setText("0"); // Reset to default value
                }
            } catch (NumberFormatException e) {
                // If the input is not a valid number, reset to default
                massTextField.setText("0");
            }
        });

        VBox inputPanel = new VBox(10,
                createLabeledInput("Initial Velocity", velocitySlider, velocityTextField),
                createLabeledInput("Launch Angle", angleSlider, angleTextField),
                createLabeledInput("Mass (kg)", massTextField),
                createLabeledInput("Gravity (Planet)", gravityDropdown, customGravityField)
        );
        inputPanel.getStyleClass().add("vboxPadded");

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
                double mass = Double.parseDouble(massTextField.getText());

                if (gravityDropdown.getValue().equals("Custom")) {
                    try {
                        gravity = Double.parseDouble(customGravityField.getText());
                    } catch (NumberFormatException ex) {
                        showAlert("Invalid Input", "Please enter a valid numeric value for gravity.");
                        gravity = 9.8;
                    }
                } else {
                    gravity = getGravityForPlanet(gravityDropdown.getValue());
                }

                System.out.println("Simulation started with:");
                System.out.println("Velocity: " + velocity + " m/s");
                System.out.println("Angle: " + angle + " degrees");
                System.out.println("Mass: " + mass + " kg");
                System.out.println("Gravity: " + gravity + " m/s²");

                startSimulation(velocity, angle, gravity, mass);
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
        resetButton.setOnAction(e -> resetSimulation(velocitySlider, angleSlider, gravityDropdown, customGravityField,massTextField));

        // Control Panel Layout
        HBox controlPanel = new HBox(10, launchButton, playButton, pauseButton, resetButton);
        controlPanel.setAlignment(javafx.geometry.Pos.CENTER);
        controlPanel.getStyleClass().add("hboxPadded");

        // Statistics
        Label heightLabel = createStyledLabel("Height: 0.0 m");
        Label velocityLabel = createStyledLabel("Velocity: 0.0 m/s");
        Label timeLabel = createStyledLabel("Time: 0.0 s");

        VBox statisticsPanel = new VBox(10, heightLabel, velocityLabel, timeLabel);
        statisticsPanel.getStyleClass().add("statistics-panel");

        // Layout Setup
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("vbox");
        mainLayout.setTop(statisticsPanel);
        mainLayout.setLeft(inputPanel);
        mainLayout.setCenter(visualiserBox);

        VBox bottomLayout = new VBox(10, controlPanel, createBackButton());
        bottomLayout.setAlignment(javafx.geometry.Pos.CENTER);

        mainLayout.setBottom(bottomLayout);
        Scene scene = new Scene(mainLayout, 1280, 720);

        SceneManager.registerScene(scene); // Register the scene
        ThemeManager.applyTheme(scene);   // Apply the current theme
        return scene;

    }

    // Helper Methods
    private Slider createSlider(double min, double max, double value) {
        Slider slider = new Slider(min, max, value);
        slider.getStyleClass().add("slider");
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

    public HBox createLabeledInput(String label, Control input) {
        // Check if inputControl is null
        if (input == null) {
            throw new IllegalArgumentException("Input control cannot be null!");
        }

        // Create the HBox layout and add the label and input control
        HBox hbox = new HBox(10);
        hbox.getChildren().add(new Label(label));
        hbox.getChildren().add(input);
        return hbox;
    }



    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("button");
        button.setMinWidth(200);
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

    private void startSimulation(double velocity, double angle, double gravity, double mass) {
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
                return 9.8;  // Default to Earth's gravity
        }
    }

    private void resetSimulation(Slider velocitySlider, Slider angleSlider, ComboBox<String> gravityDropdown, TextField customGravityField, TextField mass) {
        // Reset sliders to default values
        velocitySlider.setValue(0); // Default velocity
        angleSlider.setValue(0); // Default angle
        gravityDropdown.setValue("Earth"); // Default gravity
        customGravityField.clear(); // Clear custom gravity input field
        mass.clear(); // Clear mass input field
        System.out.println("Simulation reset to default values!");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
