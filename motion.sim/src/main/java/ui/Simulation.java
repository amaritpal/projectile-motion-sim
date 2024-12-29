package ui;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import logic.Visualiser;
import logic.PhysicsEngine;
import utils.SceneManager;
import utils.SettingsManager;
import utils.ThemeManager;

import javax.xml.stream.XMLStreamConstants;
import java.awt.event.KeyEvent;

/**
 * This class represents the simulation scene for a 2D projectile motion simulation.
 * It includes the user interface components for inputting initial velocity, launch angle, and gravity, as well as buttons for launching, playing, pausing, and resetting the simulation.
 * The simulation scene is created using JavaFX and is displayed within a JavaFX Stage.
 * It utilizes the Visualiser and PhysicsEngine classes to visualize the projectile's trajectory and calculate its motion based on the provided parameters.
 * The class also includes helper methods for creating UI components, handling user interactions,
 * and resetting the simulation to its default values.
 */
public class Simulation {
    private final Stage primaryStage;
    private final Visualiser visualiser;
    private boolean isRunning = false; // To track the simulation state
    private AnimationTimer animationTimer;
    private PhysicsEngine physicsEngine;
    private String currentLineStyle = "Solid"; // Default trajectory line style

    // Labels for real-time statistics
    private final Label heightLabel = createStyledLabel("Height: 0.0 m");
    private final Label velocityLabel = createStyledLabel("Velocity: 0.0 m/s");
    private final Label timeLabel = createStyledLabel("Time: 0.0 s");

    public Simulation(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.visualiser = new Visualiser(850, 500);
    }

    /**
     * Creates and returns the simulation scene as a JavaFX Scene object.
     * The scene includes the user interface components for inputting parameters,
     * launching the simulation, and controlling its playback.
     * @return The simulation scene as a JavaFX Scene object.
     */
    public Scene createSimulationScene() {
	    // Visualiser Section
	    VBox visualiserBox = visualiser.createVisualiserBox();

	    // Input Fields Section
	    Slider velocitySlider = createSlider(100, 0);
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

	    Slider angleSlider = createSlider(90, 45);
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

	    // Move toggle buttons below input fields
	    Button toggleGridButton = createStyledButton("Toggle Grid");
	    Button toggleAxesButton = createStyledButton("Toggle Axes");
	    Button changeLineStyleButton = createStyledButton("Change Trajectory Line Style");

	    VBox toggle = new VBox(10, toggleGridButton, toggleAxesButton, changeLineStyleButton);
	    inputPanel.getChildren().add(toggle); // Add toggle buttons below input fields

	    toggleGridButton.setOnAction(e -> visualiser.toggleGrid());
	    toggleAxesButton.setOnAction(e -> visualiser.toggleAxes());
	    changeLineStyleButton.setOnAction(e -> {
		    currentLineStyle = visualiser.toggleLineStyle();
		    System.out.println("Trajectory Line Style: " + currentLineStyle);
	    });

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

			    double maxRange = physicsEngine.calculateRange();
			    double maxHeight = physicsEngine.calculateMaxHeight();
			    visualiser.setMaxBounds(maxRange, maxHeight);

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
	    controlPanel.setAlignment(Pos.CENTER);
	    controlPanel.getStyleClass().add("hboxPadded");

	    // Statistics Panel
	    VBox statisticsPanel = new VBox(10, heightLabel, velocityLabel, timeLabel);
	    statisticsPanel.getStyleClass().add("statistics-panel");

	    BorderPane mainLayout = new BorderPane();
	    mainLayout.getStyleClass().add("vbox");
	    mainLayout.setTop(statisticsPanel);
	    mainLayout.setLeft(inputPanel);
	    mainLayout.setCenter(visualiserBox);

	    VBox bottomLayout = new VBox(10, controlPanel, createBackButton());
	    bottomLayout.setAlignment(Pos.CENTER);
	    mainLayout.setBottom(bottomLayout);

	    Scene scene = new Scene(mainLayout, 1280, 720);
	    SceneManager.registerScene(scene);
	    ThemeManager.applyTheme(scene);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.E) {
                    java.awt.EventQueue.invokeLater(() -> {
                        isRunning = !isRunning;
                        if (isRunning) {
                            startAnimation();
                        } else {
                            stopAnimation();
                        }
                    });
            }
        });

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

                    if (physicsEngine.hasProjectileHitGround() || y < 0) {
                        stopAnimation();
                        isRunning = false;
                    } else {
                        visualiser.drawPoint(x, y);
                        updateStatistics(); // Update statistics panel in real time
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

    private void updateStatistics() {
        if (physicsEngine != null) {
            heightLabel.setText(String.format("Height: %.2f m", physicsEngine.getCurrentHeight()));
            velocityLabel.setText(String.format("Velocity: %.2f m/s", physicsEngine.getCurrentVelocity()));
            timeLabel.setText(String.format("Time: %.2f s", physicsEngine.getElapsedTime()));
        }
    }

    // Helper Methods
    private Slider createSlider(double max, double value) {
        Slider slider = new Slider(0, max, value);
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
        inputRow.setAlignment(Pos.CENTER_LEFT);
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
        backButtonBox.setAlignment(Pos.CENTER);
        return backButtonBox;
    }

    private void goBackToMainMenu() {
        SceneManager.removeScene(primaryStage.getScene());
        MainMenu mainMenu = new MainMenu(primaryStage);
        primaryStage.setScene(mainMenu.createMainMenuScene());
    }

    private double getGravityForPlanet(String planet) {
	    return switch (planet) {
		    case "Earth" -> 9.8;
		    case "Moon" -> 1.6;
		    case "Mars" -> 3.7;
		    default -> 9.8;  // Default to Earth's gravity
	    };
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

        isRunning = false;
        if (animationTimer != null) animationTimer.stop();

        visualiser.resetScaling();
        visualiser.redrawCanvas();

        heightLabel.setText("Height: 0.0 m");
        velocityLabel.setText("Velocity: 0.0 m/s");
        timeLabel.setText("Time: 0.0 s");
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
