package data;

import com.google.gson.annotations.Expose;
import java.util.List;

public final class GraphData {
    @Expose
    private final List<DataPoint> trajectoryData;
    @Expose
    private final double maxX;
    @Expose
    private final double maxY;
    @Expose
    private final double launchAngle;
    @Expose
    private final double initialVelocity;
    @Expose
    private final double gravity;

    public GraphData(List<DataPoint> trajectoryData, double maxX, double maxY, double launchAngle, double initialVelocity, double gravity) {
        this.trajectoryData = trajectoryData;
        this.maxX = maxX;
        this.maxY = maxY;
        this.launchAngle = launchAngle;
        this.initialVelocity = initialVelocity;
        this.gravity = gravity;
    }

    @Override
    public String toString() {
        return String.format("Graph: Initial Velocity = %.2f m/s, Launch Angle = %.2f°, Gravity = %.2f m/s²",
                initialVelocity, launchAngle, gravity);
    }

    public List<DataPoint> trajectoryData() {
        return trajectoryData;
    }

    public double maxX() {
        return maxX;
    }

    public double maxY() {
        return maxY;
    }

    public double launchAngle() {
        return launchAngle;
    }

    public double initialVelocity() {
        return initialVelocity;
    }

    public double gravity() {
        return gravity;
    }

}
