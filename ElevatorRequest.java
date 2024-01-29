/**
 * Represents a single line of the input data, which is a request for the elevator to come.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class ElevatorRequest {

    private String timestamp;
    private Direction direction;
    private int origin;
    private int destination;

    public ElevatorRequest(String input_line) {
        String[] elements = input_line.split(" ");
        this.timestamp = elements[0];
        this.origin = Integer.parseInt(elements[1]);
        this.direction = Direction.valueOf(elements[2]);
        this.destination = Integer.parseInt(elements[3]);
    }
}

/**
 * Represents a direction for the elevator to travel in.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
enum Direction {
    Up,
    Down,
}