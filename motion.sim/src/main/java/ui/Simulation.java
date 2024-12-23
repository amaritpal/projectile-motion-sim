package ui;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logic.Visualiser;
import logic.PhysicsEngine;
import utils.SceneManager;
import utils.SettingsManager;
import utils.ThemeManager;

/**
 * This class represents the simulation scene for a 2D projectile motion simulation.
 * It includes the user interface components for inputting initial velocity, launch angle,
 * and gravity, as well as buttons for launching, playing, pausing, and resetting the simulation.
 *
 * The simulation scene is created using JavaFX and is displayed within a JavaFX Stage.
 * It utilizes the Visualiser and PhysicsEngine classes to visualize the projectile's trajectory
 * and calculate its motion based on the provided parameters.
 *
 * The class also includes helper methods for creating UI components, handling user interactions,
 * and resetting the simulation to its default values.
 */
public class Simulation {
    private final Stage primaryStage;
    private final Visualiser visualiser;
    private boolean isRunning = false; // To track the simulation state
    private AnimationTimer animationTimer;
    private PhysicsEngine physicsEngine;

    public Simulation(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.visualiser = new Visualiser(800, 400);
    }

    /**
     * Creates and returns the simulation scene as a JavaFX Scene object.
     * The scene includes the user interface components for inputting parameters,
     * launching the simulation, and controlling its playback.
     *
     * @return The simulation scene as a JavaFX Scene object.
     */
    public Scene createSimulationScene() {
        // Visualiser Section
        VBox visualiserBox = visualiser.createVisualiserBox();

        // Input Fields Section
        Slider velocitySlider = createSlider(0, 100, 0);
        TextField velocityTextField = createTextField("0");

        velocitySlider.valueProperty().addListener((obs, oldValue, newValue) ->
                velocityTextField.setText(String.valueOf(newValue.intValue()))
        );

        velocityTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                double value = Double.parseDouble(newValue);
                if (value >= 0 && value <= 100) {
                    velocitySlider.setValue(value);
                } else {
                    velocityTextField.setText(String.valueOf((int) velocitySlider.getValue()));
                }
            } catch (NumberFormatException e) {
                velocityTextField.setText(String.valueOf((int) velocitySlider.getValue()));
            }
        });

        Slider angleSlider = createSlider(0, 90, 45);
        TextField angleTextField = createTextField("45");

        angleSlider.valueProperty().addListener((obs, oldValue, newValue) ->
                angleTextField.setText(String.valueOf(newValue.intValue()))
        );

        angleTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                double value = Double.parseDouble(newValue);
                if (value >= 0 && value <= 90) {
                    angleSlider.setValue(value);
                } else {
                    angleTextField.setText(String.valueOf((int) angleSlider.getValue()));
                }
            } catch (NumberFormatException e) {
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

        VBox inputPanel = new VBox(10,
                createLabeledInput("Initial Velocity", velocitySlider, velocityTextField),
                createLabeledInput("Launch Angle", angleSlider, angleTextField),
                createLabeledInput("Gravity (Planet)", gravityDropdown, customGravityField)
        );
        inputPanel.getStyleClass().add("vboxPadded");

        // Control Buttons Section
        Button launchButton = createStyledButton("Launch");
        Button playButton = createStyledButton("Play");
        Button pauseButton = createStyledButton("Pause");
        Button resetButton = createStyledButton("Reset");

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
                        showAlert("Invalid Input", "Please enter a valid numeric value for gravity.");
                        gravity = 9.8;
                    }
                } else {
                    gravity = getGravityForPlanet(gravityDropdown.getValue());
                }

                int fps = SettingsManager.getFPS();
                physicsEngine = new PhysicsEngine(velocity, angle, gravity, fps);
                startAnimation();
            }
        });

        playButton.setOnAction(e -> {
            if (!isRunning) {
                isRunning = true;
                startAnimation();
            }
        });

        pauseButton.setOnAction(e -> {
            if (isRunning) {
                isRunning = false;
                stopAnimation();
            }
        });

        resetButton.setOnAction(e -> resetSimulation(velocitySlider, angleSlider, gravityDropdown, customGravityField));

        HBox controlPanel = new HBox(10, launchButton, playButton, pauseButton, resetButton);
        controlPanel.setAlignment(javafx.geometry.Pos.CENTER);
        controlPanel.getStyleClass().add("hboxPadded");

        // Statistics Panel
        Label heightLabel = createStyledLabel("Height: 0.0 m");
        Label velocityLabel = createStyledLabel("Velocity: 0.0 m/s");
        Label timeLabel = createStyledLabel("Time: 0.0 s");

        VBox statisticsPanel = new VBox(10, heightLabel, velocityLabel, timeLabel);
        statisticsPanel.getStyleClass().add("statistics-panel");

        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("vbox");
        mainLayout.setTop(statisticsPanel);
        mainLayout.setLeft(inputPanel);
        mainLayout.setCenter(visualiserBox);

        VBox bottomLayout = new VBox(10, controlPanel, createBackButton());
        bottomLayout.setAlignment(javafx.geometry.Pos.CENTER);
        mainLayout.setBottom(bottomLayout);

        Scene scene = new Scene(mainLayout, 1280, 720);

        SceneManager.registerScene(scene);
        ThemeManager.applyTheme(scene);
        return scene;
    }

    /**
     * Starts the animation timer to update the projectile's position and redraw the canvas.
     * The animation is controlled by the isRunning flag and the physicsEngine object.
     */

    private void startAnimation() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (physicsEngine != null) {
                    physicsEngine.updateTime();
                    double x = physicsEngine.calculateX();
                    double y = physicsEngine.calculateY();

                    visualiser.drawPoint(x, y);

                    if (physicsEngine.hasProjectileHitGround()) {
                        stopAnimation();
                        isRunning = false;
                    }
                }
            }
        };
        animationTimer.start();
    }

    private void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
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

    private void goBackToMainMenu() {
        SceneManager.removeScene(primaryStage.getScene());
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

    /**
     * Resets the simulation to its default values by clearing the canvas,
     * resetting the input fields, and redrawing the grid and axes.
     *
     * @param velocitySlider The slider for inputting the initial velocity.
     * @param angleSlider The slider for inputting the launch angle.
     * @param gravityDropdown The dropdown for selecting the gravity.
     * @param customGravityField The text field for inputting a custom gravity value.
     */

    private void resetSimulation(Slider velocitySlider, Slider angleSlider, ComboBox<String> gravityDropdown, TextField customGravityField) {
        velocitySlider.setValue(0);
        angleSlider.setValue(0);
        gravityDropdown.setValue("Earth");
        customGravityField.clear();

        // Clear the canvas and redraw the grid and axes
        visualiser.clearCanvas();
        visualiser.redrawCanvas();  // Add this line to trigger the grid and axes redraw

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