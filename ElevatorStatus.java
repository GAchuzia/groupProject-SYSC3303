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

    /** The current floor of the elevator. */
    private int floor;

    /** The current movement direction of the elevator. */
    private Direction direction;

    /** Flag indicating whether the elevator is shut down. */
    private boolean shut_down;

    /** The number of passengers currently in the elevator. */
    private int riders;

    /** The identifier of the elevator this status pertains to. */
    private int elevator_id;

    /** The status of the elevator's door. */
    private int door;

    /** The designated destination floor of the elevator. */
    private int destinationFloor;

    /** The original floor from which the elevator was requested. */
    private int originFloor;

    /** Indicates whether the current request has been completed. */
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
     * Retrieves the elevator ID this status is associated with.
     *
     * @return The elevator ID.
     */
    public int getElevator() {
        return this.elevator_id;
    }

    /**
     * Retrieves the current number of riders in the elevator.
     *
     * @return The number of riders.
     */
    public int getRiders() {
        return this.riders;
    }

    /**
     * Retrieves the current floor of the elevator.
     *
     * @return The floor number.
     */
    public int getFloor() {
        return this.floor;
    }

    /**
     * Retrieves the current direction of the elevator's movement.
     *
     * @return The direction the elevator is moving in.
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Retrieves the status of the elevator's door.
     *
     * @return The door status.
     */
    public int getDoor() {
        return this.door;
    }

    /**
     * Retrieves the destination floor of the current request.
     *
     * @return The destination floor number.
     */
    public int getDestinationFloor() {
        return this.destinationFloor;
    }

    /**
     * Retrieves the origin floor of the current request.
     *
     * @return The origin floor number.
     */
    public int getOriginFloor() {
        return this.originFloor;
    }

    /**
     * Checks if the current request has been completed.
     *
     * @return True if complete, false otherwise.
     */
    public boolean isComplete() {
        return this.complete;
    }

    /**
     * Checks if the elevator is marked as shut down.
     *
     * @return True if the elevator is shut down, false otherwise.
     */
    public boolean isShutDown() {
        return this.shut_down;
    }

    // Setters

    /**
     * Sets the elevator ID for this status.
     *
     * @param new_id The new elevator ID.
     */
    public void setElevator(int new_id) {
        this.elevator_id = new_id;
    }

    /**
     * Sets the number of riders currently in the elevator.
     *
     * @param riders The new number of riders.
     */
    public void setRiders(int riders) {
        this.riders = riders;
    }

    /**
     * Updates the floor number the elevator is on.
     *
     * @param floor The new floor number.
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * Updates the direction of the elevator's movement.
     *
     * @param direction The new movement direction.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Sets the status of the elevator's door.
     *
     * @param door The new door status.
     */
    public void setDoor(int door) {
        this.door = door;
    }

    /**
     * Sets the destination floor for the current request.
     *
     * @param destinationFloor The destination floor number.
     */
    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    /**
     * Sets the origin floor for the current request.
     *
     * @param originFloor The origin floor number.
     */
    public void setOriginFloor(int originFloor) {
        this.originFloor = originFloor;
    }

    /**
     * Marks the current request as complete or incomplete.
     *
     * @param complete True to mark the request as complete, false otherwise.
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * Marks the elevator as shut down.
     */
    public void markShutDown() {
        this.shut_down = true;
    }

    /** String representation of the state. */
    public String toString() {
        return "Status(id=" + this.elevator_id + ", floor=" + this.floor + ", direction=" + this.direction
                + ", shut_down=" + this.shut_down + ", riders=" + this.riders + ")";
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
