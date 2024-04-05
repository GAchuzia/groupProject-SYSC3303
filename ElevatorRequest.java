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
     * The time that the elevator request was made.
     */
    private LocalTime timestamp;

    /**
     * The direction that the requester wants to travel in.
     */
    private Direction direction;

    /**
     * The origin floor for the elevator request.
     */
    private int origin;

    /**
     * The destination floor for the elevator request.
     */
    private int destination;

    /**
     * The ID of the elevator this request is associated with.
     */
    private int elevator;

    /** Tracks whether or not this request is complete. */
    private boolean complete = false;

    /** Track whether there is a fault with the elevator timer. */
    private boolean timerFault = false;

    /** The number of riders in the elevator this request is being processed by. */
    private int riders;

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
     * Constructs a new ElevatorRequest that describes the current location of an
     * elevator.
     * 
     * @param elevator    The elevator ID that this request is associated with.
     * @param origin      The current floor that the elevator is on.
     * @param destination The floor the elevator is heading towards.
     */
    public ElevatorRequest(int elevator, int origin, int destination) {
        this.elevator = elevator;
        this.origin = origin;
        this.destination = destination;
        if (origin - destination > 0) {
            this.direction = Direction.Down;
        } else {
            this.direction = Direction.Up;
        }
        this.timestamp = LocalTime.now();
        this.riders = 0;
    }

    /**
     * Constructs an elevator request dataclass from a line of the input file.
     *
     * @param input_line A line from the input file in the format [timestamp]
     *                   [origin] [destination] [direction].
     *                   Example: "14:05:15.2 2 Up 4"
     */
    public ElevatorRequest(String input_line) {
        String[] elements = input_line.split(" ");
        this.timestamp = LocalTime.parse(elements[0], PARSER);
        this.origin = Integer.parseInt(elements[1]);
        this.direction = Direction.valueOf(elements[2]);
        this.destination = Integer.parseInt(elements[3]);
        this.elevator = -1; // No ID set when created from file line
        this.riders = 0;
    }

    /**
     * Constructs an elevator request from a byte array.
     *
     * @param bytes The byte array in which the elevator request is encoded.
     */
    public ElevatorRequest(byte[] bytes) throws UnsupportedEncodingException {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);

        buffer.put(bytes);
        buffer.position(0); // Start parsing from beginning

        this.origin = buffer.getInt();
        this.destination = buffer.getInt();
        this.elevator = buffer.getInt();
        this.complete = buffer.getInt() == 1;
        this.timerFault = buffer.getInt() == 1;
        this.direction = Direction.values()[buffer.getInt()];
        this.riders = buffer.getInt();

        buffer.compact(); // Compact array so remaining data is timestamp

        // Remove excess null terminating characters
        byte[] timestamp_bytes = buffer.array();
        int i = 0;
        for (; timestamp_bytes[i] != 0; i++)
            ;
        String timestamp_text = new String(timestamp_bytes, 0, i, "US-ASCII");
        this.timestamp = LocalTime.parse(timestamp_text);
    }

    /**
     * Get the timestamp of the elevator request.
     *
     * @return The timestamp of the elevator request.
     */
    public LocalTime getTimestamp() {
        return this.timestamp;
    }

    /**
     * Get the origin floor of the elevator request.
     *
     * @return The origin floor of the elevator request.
     */
    public int getOriginFloor() {
        return this.origin;
    }

    /**
     * Get the destination floor of the elevator request.
     *
     * @return The destination floor of the elevator request.
     */
    public int getDestinationFloor() {
        return this.destination;
    }

    /**
     * Get the direction of the elevator request.
     *
     * @return The direction of the elevator request.
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Sets the ID of the elevator this request is associated with.
     * 
     * @param elevator The ID of the elevator to associate this request with.
     */
    public void setElevator(int elevator) {
        this.elevator = elevator;
    }

    /**
     * Gets the ID of the elevator this request is associated with.
     * 
     * @return the ID of the elevator this request is associated with.
     */
    public int getElevator() {
        return this.elevator;
    }

    /**
     * Checks if this request has been completed.
     * 
     * @return True if this request is complete, false otherwise.
     */
    public boolean isComplete() {
        return this.complete;
    }

    /**
     * Marks this request as complete.
     */
    public void markComplete() {
        this.complete = true;
    }

    /**
     * Sets the timer fault flag.
     * 
     * @param val The new value for the flag.
     */
    public void setTimerFault(boolean val) {
        this.timerFault = val;
    }

    /**
     * Sets the direction of the elevator request.
     * 
     * @param direction The new direction of the request.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Returns whether or not the elevator had a timer fault.
     * 
     * @return True if the request has marked an elevator with a fault, false
     *         otherwise.
     */
    public boolean getTimerFault() {
        return this.timerFault;
    }

    /**
     * Gets the number of riders on the elevator that this request is associated
     * with.
     *
     * @return The number of riders on the elevator.
     */
    public int getRiders() {
        return this.riders;
    }

    /**
     * Sets the number of riders on the elevator that this request is associated
     * with.
     *
     * @param count The new number of riders.
     */
    public void setRiders(int count) {
        this.riders = count;
    }

    /**
     * Creates the string representation of an elevator request.
     *
     * @return The string representation of an elevator request.
     */
    @Override
    public String toString() {
        String completeness = this.complete ? "Complete" : "Incomplete";
        String self = "Timestamp: " + this.timestamp + " Direction: " + this.direction;
        self += " To: " + this.destination + " From: " + this.origin + " | " + completeness;
        return self;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        return this.destination == ((ElevatorRequest) obj).getDestinationFloor()
                && this.origin == ((ElevatorRequest) obj).getOriginFloor()
                && this.timestamp.equals(((ElevatorRequest) obj).getTimestamp())
                && this.direction == ((ElevatorRequest) obj).direction;
    }

    /**
     * Encodes the ElevatorRequest as bytes.
     * 
     * @return A byte array of the encoded elevator request.
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
        buffer.put(this.timestamp.toString().getBytes());
        return buffer.array();
    }
}
