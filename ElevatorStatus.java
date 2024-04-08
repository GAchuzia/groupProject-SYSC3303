import java.nio.ByteBuffer;

/**
 * For tracking the status of an elevator within the scheduler.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class ElevatorStatus {

    /** The floor the elevator is currently on. */
    private int floor;

    /** The direction the elevator is moving in. */
    private Direction direction;

    /** Whether or not the elevator has been shut down. */
    private boolean shut_down;

    /** The number of riders currently on the elevator. */
    private int riders;

    /** The ID of the elevator this status is associated with. */
    private int elevator_id;

    private int door;

    private int destinationFloor;
    private int originFloor;
    private boolean complete;

    /**
     * Creates a new status object to track an elevator's position, starting the
     * elevator at the ground floor and moving in the upward direction.
     */
    public ElevatorStatus() {
        this.floor = 1; // Assume ground floor
        this.direction = Direction.Up; // The elevator can only go up from the ground floor
        this.shut_down = false; // Assume elevator starts as active
        this.riders = 0; // No passengers to start
        this.door = 0;
        this.destinationFloor = 0;
        this.originFloor = 0;
        this.complete = false;
    }

    /**
     * Creates a new status object from a byte array representing an encoded status
     * object.
     */
    public ElevatorStatus(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.position(0);
        this.floor = buffer.getInt();
        this.direction = Direction.values()[buffer.getInt()];
        this.shut_down = buffer.getInt() == 1;
        this.riders = buffer.getInt();
        this.elevator_id = buffer.getInt();
        this.door = buffer.getInt();
        this.destinationFloor = buffer.getInt();
        this.originFloor = buffer.getInt();
        this.complete = buffer.getInt() == 1;
    }

    /**
     * Get the elevator ID associated with this status.
     * 
     * @return The elevator ID associated with this status.
     */
    public int getElevator() {
        return this.elevator_id;
    }

    /**
     * Set the elevator ID associated with this status.
     * 
     * @param new_id The elevator ID to be associated with this status.
     */
    public void setElevator(int new_id) {
        this.elevator_id = new_id;
    }

    /**
     * Get the number of riders currently on the elevator.
     * 
     * @return The number of riders currently on the elevator.
     */
    public int getRiders() {
        return this.riders;
    }

    /**
     * Set the number of riders currently on the elevator.
     */
    public void setRiders(int riders) {
        this.riders = riders;
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

    public int getDoor() {
        return this.door;
    }
    public int getDestinationFloor() {
        return this.destinationFloor;
    }
    public int getOriginFloor() {
        return this.originFloor;
    }
    public boolean isComplete() {
        return this.complete;
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

    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }
    public void setOriginFloor(int originFloor) {
        this.originFloor = originFloor;
    }
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
    public void setDoor(int door) {
        this.door = door;
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

    /** String representation of the state. */
    public String toString() {
        return "Status(id= " + this.elevator_id + ", floor=" + this.floor + ", direction=" + this.direction + ", shut_down=" + this.shut_down
                + ", riders=" + this.riders + ")";
    }

    /**
     * Encodes the status object as bytes for UDP transmission.
     *
     * @return An array of bytes representing the encoded status object.
     */
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.putInt(this.floor);
        buffer.putInt(this.direction.ordinal());
        buffer.putInt(this.shut_down ? 1 : 0);
        buffer.putInt(this.riders);
        buffer.putInt(this.elevator_id);
        buffer.putInt(this.door);
        buffer.putInt(this.destinationFloor);
        buffer.putInt(this.originFloor);
        buffer.putInt(this.complete ? 1 : 0);
        return buffer.array();
    }
}
