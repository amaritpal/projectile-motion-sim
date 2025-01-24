package data;

public class DataPoint {
	private final double x;
	private final double y;
	private final double time;
	private final double velocity;

	public DataPoint(double x, double y, double time, double velocity) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.velocity = velocity;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getTime() {return time;}

	public double getVelocity() {return velocity;}

	public String getTooltipText() {
		return String.format("Time: %.2f s\nVelocity: %.2f m/s\nRange: %.2f m\nHeight: %.2f m", time, velocity, x, y);
	}
}
