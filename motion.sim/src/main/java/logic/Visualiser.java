package logic;

import data.DataPoint;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import java.util.*;

public class Visualiser {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final double canvasWidth;
    private final double canvasHeight;

    private List<DataPoint> trajectoryData;
    private final Pane hoverPane;

    private boolean showGrid;
    private boolean showAxes;
    private double xScalingFactor;
    private double yScalingFactor;

    private double maxX;
    private double maxY;
    private boolean showToolTips;


    public Visualiser(double width, double height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.canvas = new Canvas(canvasWidth, canvasHeight);
        this.gc = canvas.getGraphicsContext2D();
        this.showGrid = true;
        this.showAxes = true;
        this.trajectoryData = new ArrayList<>();
        this.showToolTips = true;

        this.hoverPane = new Pane();
        this.hoverPane.setPrefSize(canvasWidth, canvasHeight);
        this.hoverPane.setStyle("-fx-background-color: transparent;");

        this.maxX = 100;
        this.maxY = 100;
        this.xScalingFactor = canvasWidth / maxX;
        this.yScalingFactor = canvasHeight / maxY;

        redrawCanvas();
    }

    public StackPane createVisualiserBox() {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().addAll(canvas, hoverPane);
        return stackPane;
    }

    public void setMaxBounds(double maxX, double maxY) {
        this.maxX = Math.max(this.maxX, maxX * 1.1);
        this.maxY = Math.max(this.maxY, maxY * 1.1);
        calculateScalingFactors();
        redrawCanvas();
    }

    private void calculateScalingFactors() {
        this.xScalingFactor = canvasWidth / maxX;
        this.yScalingFactor = canvasHeight / maxY;
    }

    public void addTrajectoryData(List<DataPoint> dataPoints) {
        if (dataPoints == null || dataPoints.isEmpty()) {
            System.err.println("addTrajectoryData: Received empty or null data points.");
        } else {
            this.trajectoryData = dataPoints;
            drawTooltipTrajectory();
        }
    }


    public void drawTooltipTrajectory() {
        hoverPane.getChildren().clear(); // Clear previous hover points
        for (DataPoint point : trajectoryData) {
            double[] coords = transformCoordinates(point);
            drawPoint(point, coords, showToolTips);
        }
    }

    public double[] transformCoordinates(DataPoint dataPoint) {
        // Adjust for logical-to-visual mapping
        double adjustedX = dataPoint.getX() * xScalingFactor;
        double adjustedY = canvasHeight - (dataPoint.getY() * yScalingFactor);

        // Add dynamic padding based on axes/margins
        double xPadding = 29;
        double yPadding = 0;

        // Adjust for dynamic offsets
        adjustedX += xPadding;
        adjustedY -= yPadding;

        // Round to avoid floating-point inconsistencies
        adjustedX = Math.round(adjustedX * 100.0) / 100.0;
        adjustedY = Math.round(adjustedY * 100.0) / 100.0;

        return new double[]{adjustedX, adjustedY};
    }

    public void clearCanvas() {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        hoverPane.getChildren().clear();
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
        drawTooltipTrajectory();
    }

    public void toggleGrid() {
        showGrid = !showGrid;
        redrawCanvas();
    }

    public void toggleAxes() {
        showAxes = !showAxes;
        redrawCanvas();
    }

    public void toggleToolTips() {
        showToolTips = !showToolTips;
        redrawCanvas();
        System.out.println("Tooltips " + (showToolTips ? "enabled" : "disabled"));
    }

    private void drawGrid() {
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.2);

        double spacing = canvasWidth / 20;

        for (double i = 0; i <= canvasWidth; i += spacing) {
            gc.strokeLine(i, 0, i, canvasHeight);
            gc.strokeLine(0, i, canvasWidth, i);
        }
    }

    private void drawAxes() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        gc.strokeLine(0, canvasHeight, canvasWidth, canvasHeight);
        gc.strokeLine(0, 0, 0, canvasHeight);
        drawAxisLabels();
    }

    private void drawAxisLabels() {
        gc.setFont(new Font(10));
        gc.setFill(Color.WHITE);

        int numLabels = 20;
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

    public void drawPoint(DataPoint dataPoint, double[] coords, boolean withTooltip) {
        Circle dot = new Circle(coords[0], coords[1], 5);
        dot.setFill(Color.RED);

        // Add tooltip if enabled
        if (withTooltip) {
            Tooltip tooltip = new Tooltip(dataPoint.getTooltipText());
            Tooltip.install(dot, tooltip);

            final boolean[] tooltipVisible = {false};

            // Show tooltip on hover
            dot.setOnMouseEntered(e -> {
                dot.setStroke(Color.GREEN); // Highlight the dot
                if (!tooltipVisible[0]) {
                    tooltip.setShowDelay(javafx.util.Duration.seconds(0));
                    tooltip.show(dot, e.getScreenX() + 10, e.getScreenY() + 10);
                    tooltipVisible[0] = true;
                }
            });

            dot.setOnMouseExited(e -> {
                dot.setStroke(Color.RED); // Reset stroke color
                tooltip.hide();
                tooltipVisible[0] = false;
            });
        }
        hoverPane.getChildren().add(dot);
    }

    public boolean getToolTip() {
        return showToolTips;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }


    public List<DataPoint> getTrajectoryData() {
        if (trajectoryData == null || trajectoryData.isEmpty()) {
            System.err.println("No trajectory data in Visualiser to retrieve!");
        }
        return trajectoryData;
    }

    public void drawLoaded(DataPoint dataPoint, double[] coords, boolean withTooltip) {
        Circle dot = new Circle(coords[0], coords[1], 5);
        dot.setFill(Color.BLUE);

        if (withTooltip) {
            Tooltip tooltip = new Tooltip(dataPoint.getTooltipText());
            Tooltip.install(dot, tooltip);

            final boolean[] tooltipVisible = {false};

            // Show tooltip on hover
            dot.setOnMouseEntered(e -> {
                dot.setStroke(Color.YELLOW); // Highlight the dot
                if (!tooltipVisible[0]) {
                    tooltip.setShowDelay(javafx.util.Duration.seconds(0));
                    tooltip.show(dot, e.getScreenX() + 10, e.getScreenY() + 10);
                    tooltipVisible[0] = true;
                }
            });

            dot.setOnMouseExited(e -> {
                dot.setStroke(Color.BLUE); // Reset stroke color
                tooltip.hide();
                tooltipVisible[0] = false;
            });
        }
        hoverPane.getChildren().add(dot);
    }


}
