package logic;

import data.DataPoint;

import java.util.*;

public class PhysicsEngine {
    private final double velocity;
    private final double angleRadians;
    private final double gravity;
    private final double deltaTime;
    private double currentTime;

    public PhysicsEngine(double velocity, double angleDegrees, double gravity, int fps) {
        this.velocity = velocity;
        this.angleRadians = Math.toRadians(angleDegrees);
        this.gravity = gravity;
        this.deltaTime = 1.0 / fps;
        this.currentTime = 0;
    }

    public double calculateX() {
        return velocity * Math.cos(angleRadians) * currentTime;
    }

    public double calculateY() {
        return (velocity * Math.sin(angleRadians) * currentTime) - (0.5 * gravity * Math.pow(currentTime, 2));
    }

    public double calculateMaxHeight() {
        return Math.pow(velocity * Math.sin(angleRadians), 2) / (2 * gravity);
    }

    public double calculateRange() {
        return (Math.pow(velocity, 2) * Math.sin(2 * angleRadians)) / gravity;
    }

    public boolean hasProjectileHitGround() {
        return calculateY() <= 0;
    }

    public void updateTime() {
        currentTime += deltaTime;
    }

    public double getCurrentHeight() {
        return calculateY();
    }

    public double getCurrentVelocity() {
        double horizontalVelocity = velocity * Math.cos(angleRadians);
        double verticalVelocity = velocity * Math.sin(angleRadians) - gravity * currentTime;
        return Math.sqrt(Math.pow(horizontalVelocity, 2) + Math.pow(verticalVelocity, 2));
    }

    public double getElapsedTime() {
        return currentTime;
    }

    public List<DataPoint> calculateTrajectory() {
        List<DataPoint> dataPoints = new ArrayList<>();
        double time = 0;
        while (!hasProjectileHitGround()) {
            double x = velocity * Math.cos(angleRadians) * time;
            double y = (velocity * Math.sin(angleRadians) * time) - (0.5 * gravity * Math.pow(time, 2));
            double velocityAtTime = getCurrentVelocity();
            dataPoints.add(new DataPoint(x, y, time, velocityAtTime));
            time += deltaTime;
        }
        return dataPoints;
    }


}