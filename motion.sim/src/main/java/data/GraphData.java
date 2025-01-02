package data;

import java.util.List;

public class GraphData {
	public List<DataPoint> dataPoints;
	public double maxX;
	public double maxY;

	public GraphData(List<DataPoint> dataPoints, double maxX, double maxY) {
		this.dataPoints = dataPoints;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	// Getters and Setters (if needed)
	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}
}