package ui;

import data.DataLogger;
import data.GraphData;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import data.DataPoint;
import logic.PhysicsEngine;
import logic.Visualiser;
import utils.SceneManager;
import utils.SettingsManager;
import utils.ThemeManager;

import java.util.List;

public class Simulation {
    private final Stage primaryStage;
    private final Visualiser visualiser;
    private boolean isRunning = false;
    private AnimationTimer animationTimer;
    private PhysicsEngine physicsEngine;
    private final Label heightLabel = createStyledLabel("Height: 0.0 m");
    private final Label velocityLabel = createStyledLabel("Velocity: 0.0 m/s");
    private final Label timeLabel = createStyledLabel("Time: 0.0 s");
    private final DataLogger dataLogger;

    public Simulation(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.visualiser = new Visualiser(850, 500);
        this.dataLogger = new DataLogger("src/main/resources/graphs");
    }

    public Scene createSimulationScene() {
        StackPane visualiserBox = visualiser.createVisualiserBox();

        Slider velocitySlider = createSlider(200, 0);
        TextField velocityTextField = createTextField("0");
        velocitySlider.valueProperty().addListener((obs, oldValue, newValue) ->
                velocityTextField.setText(String.valueOf(newValue.intValue()))
        );
        velocityTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                double value = Double.parseDouble(newValue);
                if (value >= 0 && value <= 200) {
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

        Button launchButton = createStyledButton("Launch");
        Button playButton = createStyledButton("Play");
        Button pauseButton = createStyledButton("Pause");
        Button resetButton = createStyledButton("Reset");
        Button saveGraphButton = createStyledButton("Save Graph");
        Button loadGraphButton = createStyledButton("Load Graph");
        Button toggleGridButton = createStyledButton("Toggle Grid");
        Button toggleAxesButton = createStyledButton("Toggle Axes");
        Button toolTips = createStyledButton("Toggle ToolTips");
        VBox toggle = new VBox(10, toggleGridButton, toggleAxesButton, toolTips, saveGraphButton, loadGraphButton);
        inputPanel.getChildren().add(toggle);

        toggleGridButton.setOnAction(e -> visualiser.toggleGrid());
        toggleAxesButton.setOnAction(e -> visualiser.toggleAxes());
        toolTips.setOnAction(e -> visualiser.toggleToolTips());
        saveGraphButton.setOnAction(e -> saveGraph());
        loadGraphButton.setOnAction(e -> loadGraph());


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

                physicsEngine = new PhysicsEngine(velocity, angle, gravity, SettingsManager.getFPS());

                double maxRange = physicsEngine.calculateRange();
                double maxHeight = physicsEngine.calculateMaxHeight();
                visualiser.setMaxBounds(maxRange, maxHeight);

                List<DataPoint> dataPoints = physicsEngine.calculateTrajectory();
                visualiser.addTrajectoryData(dataPoints);

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



	private void startAnimation() {
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (physicsEngine != null) {
					physicsEngine.updateTime();

					double x = physicsEngine.calculateX();
					double y = physicsEngine.calculateY();
					double time = physicsEngine.getElapsedTime();
					double velocity = physicsEngine.getCurrentVelocity();

					if (physicsEngine.hasProjectileHitGround()) {
						stopAnimation();
						isRunning = false;
					} else {
						DataPoint dataPoint = new DataPoint(x, y, time, velocity);
						double[] coords = visualiser.transformCoordinates(dataPoint);

						visualiser.drawPoint(dataPoint, coords, visualiser.getToolTip());
						updateStatistics();
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

    private void saveGraph() {
        if (physicsEngine != null && !isRunning) {
            List<DataPoint> trajectoryData = visualiser.getTrajectoryData();
            if (trajectoryData.isEmpty()) {
                showAlert("No Data to Save", "Cannot save graph as no trajectory data is available.");
                return;
            }

            GraphData graphData = new GraphData(
                    visualiser.getTrajectoryData(),
                    visualiser.getMaxX(),
                    visualiser.getMaxY(),
                    physicsEngine.getLaunchAngle(),
                    physicsEngine.getInitialVelocity(),
                    physicsEngine.getGravity()
            );

            dataLogger.saveGraph(graphData);
            showConfirm("Graph Saved", "Graph data has been successfully saved!");
        }
    }

    private void loadGraph() {
        List<GraphData> savedGraphs = dataLogger.getSavedGraphs();

        if (!savedGraphs.isEmpty()) {
            System.out.println("Available Graphs:");
            for (int i = 0; i < savedGraphs.size(); i++) {
                System.out.printf("[%d] Launch Angle: %.1f, Initial Velocity: %.1f, Gravity: %.2f%n",
                        i + 1,
                        savedGraphs.get(i).launchAngle(),
                        savedGraphs.get(i).initialVelocity(),
                        savedGraphs.get(i).gravity()
                );
            }

            System.out.println("Select a graph number to load:");
            int selectedIndex = getUserInput(savedGraphs.size());
            if (selectedIndex != -1) {
                GraphData selectedGraph = savedGraphs.get(selectedIndex - 1);

                // Debugging: Ensure data is valid
                System.out.printf("Selected Graph: MaxX = %.2f, MaxY = %.2f\n",
                        selectedGraph.maxX(), selectedGraph.maxY());

                // Clear the canvas and set bounds
                visualiser.setMaxBounds(selectedGraph.maxX(), selectedGraph.maxY());
                visualiser.redrawCanvas();

                // Draw each point in the trajectory
                for (DataPoint point : selectedGraph.trajectoryData()) {
                    double[] coords = visualiser.transformCoordinates(point);
                    visualiser.drawPoint(point, coords, visualiser.getToolTip());
                }
                
                showConfirm("Graph Loaded", "Graph loaded and drawn successfully!");
            } else {
                showAlert("Invalid Selection", "No graph was loaded.");
            }
        } else {
            showAlert("No Graphs Found", "There are no graphs saved to load.");
        }
        primaryStage.getScene().getRoot().requestLayout();
    }


    //method to simulate user input in the console
    private int getUserInput(int maxOptions) {
        try {
            System.out.print("Enter your choice: ");
            int choice = new java.util.Scanner(System.in).nextInt();
            if (choice >= 1 && choice <= maxOptions) {
                return choice;
            }
        } catch (Exception e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }


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

    private void showConfirm(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}