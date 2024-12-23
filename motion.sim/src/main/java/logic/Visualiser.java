package logic;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Handles visualization of the projectile motion.
 * Includes grid and axis toggles and automatic scaling functionality.
 */
public class Visualiser {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final double canvasWidth;
    private final double canvasHeight;

    // Flags for grid and axis visibility
    private boolean showGrid;
    private boolean showAxes;

    // Scaling factor
    private double scalingFactor;

    // Current bounds for scaling
    private double maxX;
    private double maxY;

    public Visualiser(double width, double height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.canvas = new Canvas(canvasWidth, canvasHeight);
        this.gc = canvas.getGraphicsContext2D();
        this.scalingFactor = 2; // Default scaling
        this.showGrid = true;
        this.showAxes = true;
        this.maxX = canvasWidth * scalingFactor;
        this.maxY = canvasHeight * scalingFactor;

        // Ensure that canvas takes up the full width and height of its parent container
        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);
        // Draw initial grid and axes
        redrawCanvas();
    }

    // Creates and returns a VBox containing the visualiser canvas
    public VBox createVisualiserBox() {
        VBox visualiserBox = new VBox(canvas);
        visualiserBox.getStyleClass().add("visualiser");

        // Set alignment properties to center the canvas horizontally and vertically
        visualiserBox.setAlignment(Pos.CENTER);

        return visualiserBox;
    }

    // Toggles grid visibility
    public void toggleGrid() {
        showGrid = !showGrid;
        redrawCanvas();
    }

    // Toggles axis visibility
    public void toggleAxes() {
        showAxes = !showAxes;
        redrawCanvas();
    }

    // Draws a projectile point
    public void drawPoint(double x, double y) {
        // Adjust scaling if necessary
        if (x > maxX || y > maxY) {
            adjustScaling(x, y);
        }

        double adjustedX = x / scalingFactor;
        double adjustedY = canvasHeight - (y / scalingFactor);

        gc.setFill(Color.LIGHTGRAY);
        gc.fillOval(adjustedX - 2.5, adjustedY - 2.5, 5, 5);
    }

    // Clears the canvas without resetting the grid and axes
    public void clearCanvas() {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
    }

    // Adjusts scaling dynamically
    public void adjustScaling(double x, double y) {
        maxX = Math.max(maxX, x);
        maxY = Math.max(maxY, y);

        // Print debug info to track the size
        //System.out.println("maxX: " + maxX + ", maxY: " + maxY);

        double scalingFactorX = canvasWidth / maxX;
        double scalingFactorY = canvasHeight / maxY;
        scalingFactor = Math.min(scalingFactorX, scalingFactorY);

        // Only adjust the scaling factor if the maximum values of x and y exceed the current canvas size
        if (maxX > canvasWidth || maxY > canvasHeight) {
            //System.out.println("Scaling factor adjusted to: " + scalingFactor);

            // Redraw the entire canvas with the new scaling factor
            redrawCanvas();
        }
    }

    // Redraws grid, axes, and other static components
    public void redrawCanvas() {
        clearCanvas();
        // Ensure that the grid and axes are drawn each time we reset or change the visibility
        if (showGrid) {
            drawGrid();
        }
        if (showAxes) {
            drawAxes();
        }
    }

    // Draws a grid on the canvas
    private void drawGrid() {
        System.out.println("Canvas width: " + canvasWidth);
        System.out.println("Canvas height: " + canvasHeight);

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.2);

        // Calculate the number of gridlines based on the canvas size
        int numVerticalGridlines = (int) (canvasWidth / 25); // Increase the division factor to get more gridlines
        int numHorizontalGridlines = (int) (canvasHeight / 25); // Increase the division factor to get more gridlines

        // Calculate the starting point for the gridlines
        double startX = (canvasWidth - (numVerticalGridlines * 25)) / 2;
        double startY = (canvasHeight - (numHorizontalGridlines * 25)) / 2;

        // Draw vertical grid lines, start from the calculated starting point
        for (int i = 0; i <= numVerticalGridlines; i++) {
            double x = startX + (i * 25);
            gc.strokeLine(x, 0, x, canvasHeight); // Ensure full height is covered
        }

        // Draw horizontal grid lines, start from the calculated starting point
        for (int i = 0; i <= numHorizontalGridlines; i++) {
            double y = startY + (i * 25);
            gc.strokeLine(0, y, canvasWidth, y); // Ensure full width is covered
        }
    }

    // Draws axes on the canvas
    private void drawAxes() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);

        // Draw x-axis
        gc.strokeLine(0, canvasHeight, canvasWidth, canvasHeight);

        // Draw y-axis
        gc.strokeLine(0, 0, 0, canvasHeight);
    }
}