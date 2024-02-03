import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

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
     * Provides a means to parse the input file's elevator request timestamps into Java's LocalTime object.
     */
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
            .appendPattern("H:m:s")
            .appendLiteral(".")
            .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, false)
            .toFormatter();

    /**
     * Constructs an elevator request dataclass from a line of the input file.
     *
     * @param input_line A line from the input file in the format [timestamp] [origin] [destination] [direction].
     * Example: "14:05:15.2 2 Up 4"
     */
    public ElevatorRequest(String input_line) {
        String[] elements = input_line.split(" ");
        this.timestamp = LocalTime.parse(elements[0], PARSER);
        this.origin = Integer.parseInt(elements[1]);
        this.direction = Direction.valueOf(elements[2]);
        this.destination = Integer.parseInt(elements[3]);
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
     * Creates the string representation of an elevator request.
     *
     * @return The string representation of an elevator request.
     */
    @Override
    public String toString() {
        String self = "Timestamp: " + this.timestamp + " Direction: " + this.direction;
        self += " To: " + this.destination + " From: " + this.origin;
        return self;
    }
}