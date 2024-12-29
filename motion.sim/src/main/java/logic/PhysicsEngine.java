package logic;

public class PhysicsEngine {
    private final double velocity;      // Initial velocity (m/s)
    private final double angleRadians; // Launch angle in radians
    private final double gravity;       // Gravity constant (m/s^2)
    private final double deltaTime;     // Time increment per update
    private double currentTime;   // Elapsed time since launch

    /**
     * Constructs a new PhysicsEngine object with the given parameters.
     *
     * @param velocity The initial velocity in meters per second.
     * @param angleDegrees The launch angle in degrees.
     * @param gravity The acceleration due to gravity.
     * @param fps Frames per second for simulation.
     */
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
}