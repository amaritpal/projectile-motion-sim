package data;

import com.google.gson.annotations.Expose;
import java.util.List;

public class GraphData {
    @Expose
    private final List<DataPoint> trajectoryData;
    @Expose
    private final double maxX;
    @Expose
    private final double maxY;

    public GraphData(List<DataPoint> trajectoryData, double maxX, double maxY) {
        this.trajectoryData = trajectoryData;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public List<DataPoint> getTrajectoryData() {
        return trajectoryData;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }
}
