package logic;

public class PhysicsEngine {
    private final double velocity;      // Initial velocity (m/s)
    private final double angle;         // Launch angle (radians)
    private final double gravity;       // Gravity constant (m/s^2)
    private final double deltaTime;     // Time increment per update
    private double currentTime;   // Elapsed time since launch

    /**
     * Constructs a new PhysicsEngine object with the given parameters.
     *
     * @param velocity The initial velocity in meters per second.
     * @param angle The launch angle in degrees.
     * @param gravity The acceleration due to gravity.
     * @param fps Frames per second for simulation.
     */
    public PhysicsEngine(double velocity, double angle, double gravity, int fps) {
        this.velocity = velocity;
        this.angle = Math.toRadians(angle);
        this.gravity = gravity;
        this.deltaTime = 1.0 / fps;
        this.currentTime = 0;
    }

    public double calculateX() {
        return velocity * Math.cos(angle) * currentTime;
    }

    public double calculateY() {
        return (velocity * Math.sin(angle) * currentTime) - (0.5 * gravity * Math.pow(currentTime, 2));
    }

    public double calculateVX() {
        return velocity * Math.cos(angle);
    }

    public double calculateVY() {
        return (velocity * Math.sin(angle)) - (gravity * currentTime);
    }

    public double calculateMaxHeight() {
        return Math.pow(velocity * Math.sin(angle), 2) / (2 * gravity);
    }

    public double calculateRange() {
        return (Math.pow(velocity, 2) * Math.sin(2 * angle)) / gravity;
    }

    public boolean hasProjectileHitGround() {
        return calculateY() <= 0;
    }

    public void updateTime() {
        currentTime += deltaTime;
    }

    public void reset() {
        currentTime = 0;
    }
}