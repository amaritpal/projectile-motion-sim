package logic;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Visualiser {
    private Canvas canvas;
    private GraphicsContext gc;
    private final double canvasWidth = 800;
    private final double canvasHeight = 400;

    private double scalingFactor;

    public Visualiser() {
        canvas = new Canvas(canvasWidth, canvasHeight);
        gc = canvas.getGraphicsContext2D();
        scalingFactor = 10; // Default scaling factor
    }

    public VBox createVisualiserBox() {
        VBox visualiserBox = new VBox(canvas);
        visualiserBox.getStyleClass().add("visualiser");
        return visualiserBox;
    }

    public void drawPoint(double x, double y) {
        double adjustedX = x / scalingFactor;
        double adjustedY = canvasHeight - (y / scalingFactor);

        // Ensure the point is within canvas boundaries
        if (adjustedX >= 0 && adjustedX <= canvasWidth && adjustedY >= 0 && adjustedY <= canvasHeight) {
            gc.setFill(Color.RED);
            gc.fillOval(adjustedX, adjustedY, 5, 5); // Point size (adjust as needed)
        }
    }

    public void clearCanvas() {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
