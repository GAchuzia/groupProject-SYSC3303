import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.io.*;
import java.net.*;
import java.util.Random;

/**
 * Represents a physical or simulated elevator.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class Elevator implements Runnable {

    /** The maximum number of passengers that an elevator can hold. */
    public static final int CAPACITY_LIMIT = 5;

    /**
     * Counts the number of elevators that have been created.
     */
    private static int ELEVATOR_COUNT = 0;

    /**
     * The time it takes to travel between two floors in milliseconds.
     */
    private static final int TIME_BETWEEN_FLOORS = 2000;

    /**
     * The chances that the elevator will have a non-recoverable timer fault and
     * shut down.
     */
    private static final int CHANCE_OF_TIMER_FAULT = 3;

    /**
     * The chances that the elevator will have to try to open/close its doors again.
     */
    private static final int CHANCE_OF_DOORS_STUCK = 5;

    /**
     * The unique identifier of this elevator.
     */
    private final int id;

    /**
     * The floor that the elevator is currently on.
     */
    private int floor;

    /**
     * The current state of the elevator.
     */
    private ElevatorState state;

    /**
     * The list of requests currently being handled by the Elevator.
     */
    private ArrayList<RequestProgressWrapper> requests_in_progress;

    /** The port this elevator uses to communicate over UDP. */
    private int port;

    /** Socket used to both send and receive information */
    private DatagramSocket channel;

    /** The direction that the elevator is currently moving in. */
    private Direction direction;

    /** The length of the buffer for receiving UDP packets in bytes. */
    private static final int BUFFER_LEN = 100;

    /** The random number generator for creating faults. */
    private Random number_gen;

    private boolean door;

    private int destinationFloor;

    private int originFloor;
    private boolean finalComplete = false;
    /**
     * Constructs a new elevator.
     *
     * @param socketPort The port at which we will be communicating to the elevator
     *                   with.
     */
    public Elevator(int port) throws SocketException {

        // Construct a datagram socket and bind it to the port passed in the constructor
        this.port = port;
        this.channel = new DatagramSocket(port);

        this.id = ELEVATOR_COUNT;
        this.floor = FloorSubsystem.GROUND_FLOOR; // Assume all elevators start on the ground floor
        this.direction = Direction.Up; // Can only move since on ground floor
        this.state = ElevatorState.Idle; // Elevators start in the idle state
        this.number_gen = new Random();
        this.requests_in_progress = new ArrayList<>();
        this.door = false;
        this.destinationFloor = 0;
        this.originFloor = 0;
        ELEVATOR_COUNT++;
    }

    /**
     * Sends a UDP packet containing the elevator's current position to the elevator
     * subsystem. The elevator's current floor occupies the origin and destination
     * fields.
     */
    private void sendLocationUpdate() {

        ElevatorRequest status = new ElevatorRequest(this.id, this.floor, this.floor, this.requests_in_progress.size(), this.door, this.destinationFloor, this.originFloor, this.finalComplete);
        status.setDirection(this.direction); // Notify scheduler of direction of movement as well
        byte[] status_b = status.getBytes();
        DatagramPacket packet = new DatagramPacket(status_b, status_b.length);
        packet.setPort(ElevatorSubsystem.PORT);

        try {
            packet.setAddress(InetAddress.getLocalHost());
            this.channel.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Notifies the scheduler that the elevator has shut down and re-routes all of
     * the elevator requests.
     */
    private void sendShutdownNotice() {
        for (RequestProgressWrapper w : this.requests_in_progress) {

            ElevatorRequest r = w.getRequest(); // Extract request

            // Mark each request as being incomplete due to a timer fault
            r.setTimerFault(true);
            byte[] r_b = r.getBytes();
            DatagramPacket packet = new DatagramPacket(r_b, r_b.length);
            packet.setPort(ElevatorSubsystem.PORT);

            // Send request to elevator subsystem for routing
            try {
                packet.setAddress(InetAddress.getLocalHost());
                this.channel.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Moves the elevator to the destination floor.
     *
     * @param direction    The direction to move in.
     * @param randomNumber the random number to generate the fault for the timer
     * @return True if the movement was successful, false if a fault occurred.
     */
    boolean move(int randomNumber) {

        // Move in the correct direction
        int old_floor = this.floor;
        switch (this.direction) {

            case Direction.Up:
                this.floor++;
                break;

            case Direction.Down:
                // Shut down elevator if it tries to go below the ground floor
                if (this.floor - 1 < 1) {
                    System.out.println("Elevator tried to go below ground floor.");
                    this.sendShutdownNotice();
                    return false;
                }
                this.floor--;
        }

        System.out.println("Elevator #" + this.id + " moving from floor " + old_floor + " to " + this.floor);

        // Sleep for 1 second to simulate movement
        try {
            Thread.sleep(TIME_BETWEEN_FLOORS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // There is a chance of having a timer fault
        if (randomNumber <= CHANCE_OF_TIMER_FAULT) {
            System.out.println("Elevator #" + this.id + " timer is stuck. Shutting down elevator...");
            this.sendShutdownNotice();
            return false;
        }

        this.sendLocationUpdate(); // Update scheduler with new location
        return true;
    }

    /**
     * Opens the elevator doors.
     */
    void openDoors(int randomNumber) {
        System.out.println("Elevator #" + this.id + " opening doors.");

//        // There is a chance that the door is stuck closed
//        while (randomNumber <= CHANCE_OF_DOORS_STUCK) {
//            System.out.println("Elevator #" + this.id + " door is stuck closed. Trying again...");
//            // Keep generating a new random number until doors are opened
//            randomNumber = this.nextRandomNum();
//        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Elevator #" + this.id + " door opened");
        this.door = true;
    }

    /**
     * Closes the elevator doors.
     */
    void closeDoors(int randomNumber) {
        System.out.println("Elevator #" + this.id + " closing doors.");

//        // There is a chance that the door is stuck open
//        while (randomNumber <= CHANCE_OF_DOORS_STUCK) {
//            System.out.println("Elevator #" + this.id + " door is stuck open. Trying again...");
//            // Keep generating a new random number until doors are closed
//            randomNumber = this.nextRandomNum();
//        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Elevator #" + this.id + " door closed");
        this.door = false;

    }

    /**
     * Swaps the elevator's direction to the opposite.
     */
    void toggleDirection() {
        switch (this.direction) {
            case Direction.Up:
                this.direction = Direction.Down;
                break;
            case Direction.Down:
                this.direction = Direction.Up;
                break;
        }
    }

    /**
     * Returns the next random number in the stream.
     * 
     * @return A random number between 1 and 100.
     */
    int nextRandomNum() {
        return number_gen.nextInt(100) + 1;
    }

    /**
     * Checks if there are still floors to visit in the elevator's current
     * direction.
     * 
     * @return True if there are destination floors to visit in the direction the
     *         elevator is moving, false otherwise.
     */
    boolean floorsInDirection() {

        for (RequestProgressWrapper r : this.requests_in_progress) {
            switch (this.direction) {
                case Direction.Up:
                    if (this.floor < r.getRequest().getOriginFloor() && !r.wasPickedUp()) {
                        return true;
                    }
                    if (this.floor < r.getRequest().getDestinationFloor() && (r.wasPickedUp() && !r.isComplete())) {
                        return true;
                    }
                    break;
                case Direction.Down:
                    if (this.floor > r.getRequest().getOriginFloor() && !r.wasPickedUp()) {
                        return true;
                    }
                    if (this.floor > r.getRequest().getDestinationFloor() && (r.wasPickedUp() && !r.isComplete())) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Given the elevator's current floor, mark requests that are in progress as
     * partially complete. If any request is
     * fully complete, send it back to the floor subsystem.
     */
    void updateRequests() {

        // Update progress for all requests
        for (RequestProgressWrapper r : this.requests_in_progress) {
            r.updateProgress(this.floor);

            // If the request is now complete, send the completion back to the floor
            if (r.isComplete()) {
                this.finalComplete = true;

                System.out.println("Elevator #" + this.id + " completed request " + r.getRequest());

                byte[] data = r.getRequest().getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length);
                packet.setPort(ElevatorSubsystem.PORT);
                try {
                    packet.setAddress(InetAddress.getLocalHost());
                    this.channel.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }

        // Remove all completed requests
        for (Iterator<RequestProgressWrapper> it = this.requests_in_progress.iterator(); it.hasNext();) {
            if (it.next().isComplete()) {
                it.remove();
            }
        }
    }

    /**
     * Checks if the elevator is currently at a floor that needs to be stopped at.
     * 
     * @return True if the current floor is a stop that needs to be serviced, false
     *         otherwise.
     */
    boolean atStop() {

        for (RequestProgressWrapper r : this.requests_in_progress) {

            // This floor is the origin floor of a request who has not had a pick-up
            if (this.floor == r.getRequest().getOriginFloor() && !r.wasPickedUp()) {
                return true;
            }

            // This floor is the destination floor of a request who has not had a drop-off
            if (this.floor == r.getRequest().getDestinationFloor() && (r.wasPickedUp() && !r.isComplete())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Implements the finite state machine logic of an elevator.
     */
    public void run() {

        while (true) {

            switch (this.state) {

                case ElevatorState.Idle:

                    // If there are still requests to process, don't wait for new requests longer
                    // than 50ms
                    // This will still get new requests for handling multiple at once, but it won't
                    // wait too long
                    try {
                        if (!this.requests_in_progress.isEmpty()) {
                            channel.setSoTimeout(50);
                        } else {
                            channel.setSoTimeout(0);
                        }
                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    }

                    // Construct a DatagramPacket for receiving packets up to 100 bytes long (the
                    // length of the byte array).
                    DatagramPacket new_packet = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
                    System.out.println("Elevator #" + this.id + " Waiting for new elevator request...");

                    // Receive the packet from the scheduler
                    try {
                        channel.receive(new_packet);
                    } catch (SocketTimeoutException ignore) {
                        this.state = ElevatorState.Moving;
                        continue; // Skip to next iteration if we time out
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

                    // Parse packet
                    try {
                        ElevatorRequest new_request = new ElevatorRequest(new_packet.getData());
                        System.out.println("Elevator #" + this.id + " got request " + new_request);
                        this.destinationFloor = new_request.getDestinationFloor();
                        this.originFloor = new_request.getOriginFloor();
                        this.finalComplete = false;
                        // Add request to progress list
                        this.requests_in_progress.add(new RequestProgressWrapper(new_request));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    break;

                case ElevatorState.Moving:

                    // If there are no floors in our direction, then go the other way
                    if (!this.floorsInDirection()) {
                        this.toggleDirection();
                    }

                    // Check if moving was successful or if a fault was generated
                    if (!this.move(this.nextRandomNum())) {
                        this.state = ElevatorState.Halted;
                        break;
                    }

                    // Check if we are currently on a floor that is part of an ongoing request so we
                    // can track completion
                    if (this.atStop()) {
                        // Go open doors since someone needs to get on/off here
                        this.state = ElevatorState.DoorsOpen;
                        break;
                    }

                    // At this point we're not at a destination floor, but we've moved successfully.
                    // That means it's time to move again.
                    this.state = ElevatorState.Moving;
                    break;

                case ElevatorState.DoorsOpen:
                    this.openDoors(this.nextRandomNum());
                    this.state = ElevatorState.DoorsClosed;
                    this.sendLocationUpdate();
                    break;

                case ElevatorState.DoorsClosed:
                    this.closeDoors(this.nextRandomNum());
                    this.updateRequests(); // Update requests after every drop-off/pick-up
                    this.state = ElevatorState.Idle; // Check for a new request before moving
                    this.sendLocationUpdate();
                    break;

                case ElevatorState.Halted:
                    return; // Turn off the elevator
            }
        }
    }

    /**
     * Get the current floor of the elevator.
     *
     * @return The current floor of the elevator.
     */
    public int getFloor() {
        return this.floor;
    }

    /**
     * Gets the ID of the elevator.
     *
     * @return The unique ID of this elevator.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the port number that this elevator uses to communicate.
     * 
     * @return the port number that this elevator uses to communicate.
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Gets the current state of the elevator.
     * 
     * @return the current state of the elevator.
     */
    public ElevatorState getState() {
        return this.state;
    }

    /**
     * Get the direction of the elevator.
     *
     * @return The direction of the elevator.
     */
    public Direction getDirection() {
        return this.direction;
    }

}
