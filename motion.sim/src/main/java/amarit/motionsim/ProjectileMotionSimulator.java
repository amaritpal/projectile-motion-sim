package amarit.motionsim;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ProjectileMotionSimulator extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Projectile Motion Simulator");

        // Main Layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // === Canvas (Visualizer) ===
        Canvas canvas = new Canvas(640, 360);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        StackPane visualiserContainer = new StackPane(canvas);
        visualiserContainer.setStyle("-fx-border-color: black; -fx-border-width: 2px;");

        // === Input Panel (Right Side) ===
        VBox inputPanel = new VBox(10);
        inputPanel.setPadding(new Insets(10));
        inputPanel.setAlignment(Pos.TOP_LEFT);
        inputPanel.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-background-color: #f4f4f4;");
        inputPanel.setPrefWidth(280);

        Label lblVelocity = new Label("Velocity (m/s):");
        TextField txtVelocity = new TextField();
        Label lblAngle = new Label("Angle (°):");
        TextField txtAngle = new TextField();
        Label lblGravity = new Label("Gravity (m/s²):");
        TextField txtGravity = new TextField();
        Button btnLaunch = new Button("🚀 Launch");

        // Additional settings
        CheckBox chkShowTrajectory = new CheckBox("Show Trajectory");
        Label lblFPS = new Label("FPS:");
        ChoiceBox<String> fpsChoice = new ChoiceBox<>();
        fpsChoice.getItems().addAll("30 FPS", "60 FPS");
        fpsChoice.setValue("60 FPS");

        inputPanel.getChildren().addAll(lblVelocity, txtVelocity, lblAngle, txtAngle, lblGravity, txtGravity,
                chkShowTrajectory, lblFPS, fpsChoice, btnLaunch);

        // === Simulation Controls (Below Canvas) ===
        HBox simControls = new HBox(15);
        simControls.setAlignment(Pos.CENTER);
        Button btnPlay = new Button("▶ Play");
        Button btnPause = new Button("⏸ Pause");
        Button btnReset = new Button("🔄 Reset");
        simControls.getChildren().addAll(btnPlay, btnPause, btnReset);

        // === Graph Controls (Below Simulation Controls) ===
        HBox graphControls = new HBox(15);
        graphControls.setAlignment(Pos.CENTER);
        Button btnSaveGraph = new Button("💾 Save Graph");
        Button btnLoadGraph = new Button("📂 Load Graph");
        graphControls.getChildren().addAll(btnSaveGraph, btnLoadGraph);

        // === Music Controls (Bottom) ===
        HBox musicControls = new HBox(15);
        musicControls.setAlignment(Pos.CENTER);
        Button btnPlayMusic = new Button("🎵 Play Music");
        Button btnPauseMusic = new Button("⏸ Pause Music");
        musicControls.getChildren().addAll(btnPlayMusic, btnPauseMusic);

        // === Layout Adjustments ===
        root.setCenter(visualiserContainer);
        root.setRight(inputPanel);
        VBox bottomLayout = new VBox(10, simControls, graphControls, musicControls);
        bottomLayout.setAlignment(Pos.CENTER);
        root.setBottom(bottomLayout);

        // Scene Setup
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
