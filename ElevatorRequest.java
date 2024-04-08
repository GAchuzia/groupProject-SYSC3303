import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Represents a single line of the input data, which is a request for the
 * elevator to come.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class ElevatorRequest {

    /**
     * Timestamp of when the elevator request was initiated.
     */
    private LocalTime timestamp;

    /**
     * Intended travel direction of the request: Up or Down.
     */
    private Direction direction;

    /**
     * Starting floor from which the elevator request was made.
     */
    private int origin;

    /**
     * Target floor the request aims to reach.
     */
    private int destination;

    /**
     * Initially intended destination, can vary from {@code destination} in complex scenarios.
     */
    private int actualDestination;

    /**
     * Identifier for the elevator assigned to fulfill this request.
     */
    private int elevator;

    /**
     * Flag indicating if the request has been fulfilled.
     */
    private boolean complete = false;

    /**
     * Indicates if there's a detected fault with the timing mechanism of the elevator.
     */
    private boolean timerFault = false;

    /**
     * Count of individuals riding the elevator for this specific request.
     */
    private int riders;

    /**
     * Status of the elevator's door associated with this request; typically 0 for closed.
     */
    private int door = 0;

    /**
     * The final destination floor of the request.
     */
    private int finalDestination;

    /**
     * Original floor from which the request commenced.
     */
    private int initialOrigin;

    /**
     * Completion status for the request.
     */
    private boolean finalComplete = false;

    /**
     * Provides a means to parse the input file's elevator request timestamps into
     * Java's LocalTime object.
     */
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
            .appendPattern("H:m:s")
            .appendLiteral(".")
            .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, false)
            .toFormatter();

    /**
     * Constructs a new ElevatorRequest object with detailed parameters for immediate requests.
     *
     * @param elevator          The elevator ID this request is associated with.
     * @param origin            The floor the request originated from.
     * @param destination       The destination floor of the request.
     * @param riders            The number of riders.
     * @param door              The status of the elevator's door.
     * @param finalDestination  The final destination of the request.
     * @param initialOrigin     The initial origin floor of the request.
     * @param finalComplete     Indicates if the request has been fully completed.
     */
    public ElevatorRequest(int elevator, int origin, int destination, int riders, int door, int finalDestination, int initialOrigin, boolean finalComplete) {
        this.elevator = elevator;
        this.origin = origin;
        this.destination = destination;
        this.direction = origin - destination > 0 ? Direction.Down : Direction.Up;
        this.timestamp = LocalTime.now();
        this.riders = riders;
        this.door = door;
        this.finalDestination = finalDestination;
        this.initialOrigin = initialOrigin;
        this.finalComplete = finalComplete;
    }

    /**
     * Constructs an ElevatorRequest object from a string line typically read from an input file.
     *
     * @param input_line The string containing the request details in a predefined format.
     */
    public ElevatorRequest(String input_line) {
        String[] elements = input_line.split(" ");
        this.timestamp = LocalTime.parse(elements[0], PARSER);
        this.origin = Integer.parseInt(elements[1]);
        this.direction = Direction.valueOf(elements[2]);
        this.destination = Integer.parseInt(elements[3]);
        this.elevator = -1; // Default ID, indicating not yet assigned to an elevator.
        this.riders = 0; // Default number of riders.
        this.finalDestination = this.destination;
        this.initialOrigin = this.origin;
    }

    /**
     * Decodes an ElevatorRequest from a byte array. This is useful for networking scenarios where requests are serialized.
     *
     * @param bytes The byte array containing the serialized ElevatorRequest.
     * @throws UnsupportedEncodingException If the platform does not support the required encoding.
     */
    public ElevatorRequest(byte[] bytes) throws UnsupportedEncodingException {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes).position(0); // Prepare to read from the beginning.

        // Decode the properties from the byte buffer.
        this.origin = buffer.getInt();
        this.destination = buffer.getInt();
        this.elevator = buffer.getInt();
        this.complete = buffer.getInt() == 1;
        this.timerFault = buffer.getInt() == 1;
        this.direction = Direction.values()[buffer.getInt()];
        this.riders = buffer.getInt();
        this.door = buffer.getInt();
        this.finalDestination = buffer.getInt();
        this.initialOrigin = buffer.getInt();
        this.finalComplete = buffer.getInt() == 1;

        // Handling the timestamp decoding.
        byte[] timestamp_bytes = new byte[buffer.remaining()];
        buffer.get(timestamp_bytes);
        String timestamp_text = new String(timestamp_bytes, "US-ASCII").trim();
        this.timestamp = LocalTime.parse(timestamp_text, PARSER);
    }


    /**
     * Returns the timestamp when the elevator request was made.
     *
     * @return The timestamp of the request.
     */
    public LocalTime getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns the floor from which the elevator request was made.
     *
     * @return The origin floor of the request.
     */
    public int getOriginFloor() {
        return this.origin;
    }

    /**
     * Returns the targeted destination floor of the elevator request.
     *
     * @return The destination floor of the request.
     */
    public int getDestinationFloor() {
        return this.destination;
    }

    /**
     * Returns the adjusted final destination floor, if applicable.
     *
     * @return The final destination floor of the request.
     */
    public int getFinalDestinationFloor() {
        return this.finalDestination;
    }

    /**
     * Returns the original origin floor from which the request started.
     *
     * @return The initial origin floor of the request.
     */
    public int getInitialOriginFloor() {
        return this.initialOrigin;
    }

    /**
     * Checks if the request has been completely fulfilled, considering any adjustments or rerouting.
     *
     * @return True if the request is fully complete, false otherwise.
     */
    public boolean isFinalComplete() {
        return this.finalComplete;
    }

    /**
     * Returns the direction intended for the elevator travel.
     *
     * @return The travel direction of the request.
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Returns the elevator ID associated with this request.
     *
     * @return The ID of the elevator handling this request.
     */
    public int getElevator() {
        return this.elevator;
    }

    /**
     * Checks if the request has been fulfilled.
     *
     * @return True if the request is complete, false otherwise.
     */
    public boolean isComplete() {
        return this.complete;
    }

    /**
     * Returns whether the elevator experienced a timer fault while processing this request.
     *
     * @return True if there was a timer fault, false otherwise.
     */
    public boolean getTimerFault() {
        return this.timerFault;
    }

    /**
     * Returns the number of riders currently associated with this elevator request.
     *
     * @return The number of riders.
     */
    public int getRiders() {
        return this.riders;
    }

    /**
     * Returns the status of the elevator door associated with this request.
     *
     * @return The door status.
     */
    public int getDoor() {
        return this.door;
    }

    // Setters

    /**
     * Associates this request with an elevator by its ID.
     *
     * @param elevator The ID of the elevator to be associated with this request.
     */
    public void setElevator(int elevator) {
        this.elevator = elevator;
    }

    /**
     * Marks this request as completed.
     */
    public void markComplete() {
        this.complete = true;
    }

    /**
     * Sets the flag indicating a timer fault occurred during this request's processing.
     *
     * @param val The new value for the timer fault flag.
     */
    public void setTimerFault(boolean val) {
        this.timerFault = val;
    }

    /**
     * Sets the final completion status of this request.
     *
     * @param finalComplete True if the request is fully complete, false otherwise.
     */
    public void markFinalComplete(boolean finalComplete) {
        this.finalComplete = finalComplete;
    }

    /**
     * Updates the travel direction for this elevator request.
     *
     * @param direction The new direction of travel.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Generates a string representation of this elevator request, detailing its timestamp,
     * direction of travel, origin and destination floors, and completion status.
     *
     * @return A detailed string summarizing the elevator request.
     */
    @Override
    public String toString() {
        String completeness = this.complete ? "Complete" : "Incomplete";
        return String.format("Timestamp: %s Direction: %s To: %d From: %d | %s",
                this.timestamp, this.direction, this.destination, this.origin, completeness);
    }

    /**
     * Compares this elevator request to another object for equality. Two requests are considered equal
     * if they have the same destination and origin floors, identical timestamps, and direction of travel.
     *
     * @param obj The object to compare with this elevator request.
     * @return True if the specified object is an elevator request with the same properties, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ElevatorRequest other = (ElevatorRequest) obj;
        return this.destination == other.destination &&
                this.origin == other.origin &&
                this.timestamp.equals(other.timestamp) &&
                this.direction == other.direction;
    }


    /**
     * Encodes this ElevatorRequest into a byte array for serialization.
     *
     * @return A byte array representing this ElevatorRequest.
     */
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.putInt(this.origin);
        buffer.putInt(this.destination);
        buffer.putInt(this.elevator);
        buffer.putInt(this.complete ? 1 : 0);
        buffer.putInt(this.timerFault ? 1 : 0);
        buffer.putInt(this.direction.ordinal());
        buffer.putInt(this.riders);
        buffer.putInt(this.door);
        buffer.putInt(this.finalDestination);
        buffer.putInt(this.initialOrigin);
        buffer.putInt(this.finalComplete ? 1 : 0);
        buffer.put(this.timestamp.toString().getBytes());
        return buffer.array();
    }
}
