package logic;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class Visualiser {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final double canvasWidth;
    private final double canvasHeight;

    private boolean showGrid;
    private boolean showAxes;

    private double xScalingFactor;
    private double yScalingFactor;

    private double maxX;
    private double maxY;

    public Visualiser(double width, double height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.canvas = new Canvas(canvasWidth, canvasHeight);
        this.gc = canvas.getGraphicsContext2D();
        this.showGrid = true;
        this.showAxes = true;

        // Default scaling and bounds
        this.maxX = 100;
        this.maxY = 100;
        this.xScalingFactor = canvasWidth / maxX;
        this.yScalingFactor = canvasHeight / maxY;

        redrawCanvas();
    }

    public VBox createVisualiserBox() {
        VBox visualiserBox = new VBox(canvas);
        visualiserBox.getStyleClass().add("visualiser");
        visualiserBox.setAlignment(Pos.CENTER);
        return visualiserBox;
    }

    public void setMaxBounds(double maxX, double maxY) {
        this.maxX = maxX;
        this.maxY = maxY;

        this.xScalingFactor = canvasWidth / maxX;
        this.yScalingFactor = canvasHeight / maxY;

        redrawCanvas();
    }

    public void drawPoint(double x, double y) {
        if (x > maxX || y > maxY) return; // Prevent drawing outside bounds

        double adjustedX = (x / maxX) * canvasWidth;
        double adjustedY = canvasHeight - ((y / maxY) * canvasHeight);

        System.out.printf("Drawing point: World(%.2f, %.2f) -> Canvas(%.2f, %.2f)%n", x, y, adjustedX, adjustedY);

        gc.setFill(Color.RED);
        gc.fillOval(adjustedX - 2.5, adjustedY - 2.5, 5, 5);
    }

    public void clearCanvas() {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
    }

    public void resetScaling() {
        this.maxX = 100;
        this.maxY = 100;
        this.xScalingFactor = canvasWidth / maxX;
        this.yScalingFactor = canvasHeight / maxY;

        redrawCanvas();
    }

    public void redrawCanvas() {
        clearCanvas();
        if (showGrid) drawGrid();
        if (showAxes) drawAxes();
    }

    private void drawGrid() {
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.2);

        double spacing = canvasWidth / 10;

        for (double i = 0; i <= canvasWidth; i += spacing) {
            gc.strokeLine(i, 0, i, canvasHeight);
            gc.strokeLine(0, i, canvasWidth, i);
        }
    }

    private void drawAxes() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        gc.strokeLine(0, canvasHeight, canvasWidth, canvasHeight); // x-axis
        gc.strokeLine(0, 0, 0, canvasHeight); // y-axis
        drawAxisLabels();
    }

    private void drawAxisLabels() {
        gc.setFont(new Font(10));
        gc.setFill(Color.WHITE);

        int numLabels = 10;
        double xStep = maxX / numLabels;
        double yStep = maxY / numLabels;
        double xSpacing = canvasWidth / numLabels;
        double ySpacing = canvasHeight / numLabels;

        for (int i = 0; i <= numLabels; i++) {
            double xValue = i * xStep;
            double yValue = i * yStep;

            gc.fillText(String.format("%.1f", xValue), i * xSpacing, canvasHeight - 5);
            gc.fillText(String.format("%.1f", yValue), 5, canvasHeight - (i * ySpacing));
        }
    }
}
