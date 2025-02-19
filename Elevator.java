import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;


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

    /**
     * The status of the door represented as an Object.
     * 1: The door is open.
     * 0: The door is closed.
     * -1: The door is stuck and cannot be opened (stuck closed).
     * -2: The door is stuck and cannot be closed (stuck opened).
     */
    private int door;


    // Declare variables to hold the start and end times
    private long startTime;
    private long endTime;


    /**
     * Constructs a new elevator.
     *
     * @param port The port at which we will be communicating to the elevator
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
        this.door = 0;
        ELEVATOR_COUNT++;
    }


    // Method to start the timer
    private void startTimer() {
        startTime = System.nanoTime();
    }

    // Method to stop the timer and print the elapsed time
    private void stopTimer() {
        endTime = System.nanoTime();
        long elapsedTimeInMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("Time taken to process request: " + elapsedTimeInMillis + " milliseconds");
    }


    /**
     * Sends the elevator's current location to the elevator subsystem using UDP. The message
     * includes the elevator's ID, current floor, door status, and the number of requests in progress.
     * The elevator's current floor is used as both the origin and destination to signify its position.
     */
    private void sendLocationUpdate() {
        ElevatorRequest status = new ElevatorRequest(this.id, this.floor, this.floor, this.requests_in_progress.size(), this.door, 0, 0, false);
        status.setDirection(this.direction);
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
     * Sends an update to the elevator subsystem using UDP when a request is processed. This message
     * includes the elevator's ID, current floor, door status, number of requests in progress, and
     * details of the request being processed such as origin floor, destination floor, and completion status.
     *
     * @param originFloor The floor from which the request originated.
     * @param destinationFloor The target floor for the request.
     * @param complete Flag indicating whether the request has been completed.
     */
    private void sendRequestUpdate(int originFloor, int destinationFloor, boolean complete){
        ElevatorRequest status = new ElevatorRequest(this.id, this.floor, this.floor, this.requests_in_progress.size(), this.door, destinationFloor, originFloor, complete);
        status.setDirection(this.direction);
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

        // If the doors is stuck closed, mark them as stuck closed and send the status to elevator subsystem.
        if(randomNumber <= CHANCE_OF_DOORS_STUCK){
            this.door = -1;
            sendLocationUpdate();
        }

        // There is a chance that the door is stuck closed
        while (randomNumber <= CHANCE_OF_DOORS_STUCK) {
            System.out.println("Elevator #" + this.id + " door is stuck closed. Trying again...");
            // Keep generating a new random number until doors are opened
            randomNumber = this.nextRandomNum();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Elevator #" + this.id + " door opened");
        this.door = 1;
    }

    /**
     * Closes the elevator doors.
     */
    void closeDoors(int randomNumber) {
        System.out.println("Elevator #" + this.id + " closing doors.");

        // If the doors is stuck opened, mark them as stuck opened and send the status to elevator subsystem.
        if(randomNumber <= CHANCE_OF_DOORS_STUCK){
            this.door = -2;
            sendLocationUpdate();
        }

        // There is a chance that the door is stuck open
        while (randomNumber <= CHANCE_OF_DOORS_STUCK) {
            System.out.println("Elevator #" + this.id + " door is stuck open. Trying again...");
            // Keep generating a new random number until doors are closed
            randomNumber = this.nextRandomNum();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Elevator #" + this.id + " door closed");
        this.door = 0;

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
                r.getRequest().markFinalComplete(true);
                ElevatorRequest request = r.getRequest();

                System.out.println("Elevator #" + this.id + " completed request " + r.getRequest());
                stopTimer();
                sendRequestUpdate(request.getInitialOriginFloor(), request.getFinalDestinationFloor(), request.isFinalComplete());

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
                    try {
                        if (!this.requests_in_progress.isEmpty()) {
                            channel.setSoTimeout(50);
                        } else {
                            channel.setSoTimeout(0);
                        }
                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    }
                    DatagramPacket new_packet = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
                    System.out.println("Elevator #" + this.id + " Waiting for new elevator request...");
                    try {
                        channel.receive(new_packet);
                    } catch (SocketTimeoutException ignore) {
                        this.state = ElevatorState.Moving;
                        continue; // Skip to next iteration if we time out
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    // Start the timer when a request is received
                    startTimer();
                    try {
                        ElevatorRequest new_request = new ElevatorRequest(new_packet.getData());
                        System.out.println("Elevator #" + this.id + " got request " + new_request);
                        this.requests_in_progress.add(new RequestProgressWrapper(new_request));
                        sendRequestUpdate(new_request.getInitialOriginFloor(), new_request.getFinalDestinationFloor(), new_request.isFinalComplete());
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case ElevatorState.Moving:
                    if (!this.floorsInDirection()) {
                        this.toggleDirection();
                    }
                    if (!this.move(this.nextRandomNum())) {
                        this.state = ElevatorState.Halted;
                        break;
                    }
                    if (this.atStop()) {
                        this.state = ElevatorState.DoorsOpen;
                        break;
                    }
                    this.state = ElevatorState.Moving;
                    break;

                case ElevatorState.DoorsOpen:
                    this.openDoors(this.nextRandomNum());
                    this.state = ElevatorState.DoorsClosed;
                    this.sendLocationUpdate();
                    break;

                case ElevatorState.DoorsClosed:
                    this.closeDoors(this.nextRandomNum());
                    this.updateRequests();
                    // Stop the timer when request is complete
                    this.state = ElevatorState.Idle;
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
