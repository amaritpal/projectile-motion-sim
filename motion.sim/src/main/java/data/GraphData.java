package data;

import com.google.gson.annotations.Expose;
import java.util.List;

public record GraphData(@Expose List<DataPoint> trajectoryData, @Expose double maxX, @Expose double maxY,
                        @Expose double launchAngle, @Expose double initialVelocity, @Expose double gravity) {

    @Override
    public String toString() {
        return String.format("Graph: Initial Velocity = %.2f m/s, Launch Angle = %.2f°, Gravity = %.2f m/s²",
                initialVelocity, launchAngle, gravity);
    }
}
