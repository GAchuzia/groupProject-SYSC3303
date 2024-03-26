/**
 * For tracking the status of an elevator within the scheduler.
 */
public class ElevatorStatus {

    /** The floor the elevator is currently on. */
    private int floor;

    /** The direction the elevator is moving in. */
    private Direction direction;

    /** Whether or not the elevator has been shut down. */
    private boolean shut_down;

    /**
     * Creates a new status object to track an elevator's position, starting the
     * elevator at the ground floor and moving in the upward direction.
     */
    public ElevatorStatus() {
        this.floor = 1; // Assume ground floor
        this.direction = Direction.Up; // The elevator can only go up from the ground floor
        this.shut_down = true; // Assume elevator starts as active
    }

    /**
     * Gets the last recorded floor the elevator was at.
     * 
     * @return The last recorded floor the elevator was at.
     */
    public int getFloor() {
        return this.floor;
    }

    /**
     * Gets the last recorded direction the elevator was moving in.
     * 
     * @return The last recorded floor the elevator was moving in.
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Record the floor the elevator is on.
     * 
     * @param The floor to record.
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * Record the direction the elevator is moving in.
     * 
     * @param The direction to record.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Check if the elevator has been recorded as shutdown.
     * 
     * @return True if the elevator is shut down, false otherwise.
     */
    public boolean isShutDown() {
        return this.shut_down;
    }

    /**
     * Mark the elevator as being shut down.
     */
    public void markShutDown() {
        this.shut_down = true;
    }

    public String toString() {
        return "Status(floor=" + this.floor + ", direction=" + this.direction + ", shut_down=" + this.shut_down + ")";
    }
}
