import java.util.NavigableSet;
import java.util.TreeSet;
import java.io.*;
import java.net.*;

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

    /**
     * Counts the number of elevators that have been created.
     */
    static private int ELEVATOR_COUNT = 0;

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
     * Keeps track of the floors the elevator needs to visit.
     */
    private NavigableSet<Integer> floor_q;

    /**
     * The current request being handled by the Elevator.
     */
    private ElevatorRequest current_request;

    /** The port this elevator uses to communicate over UDP. */
    private int port;

    /** Socket used to both send and receive information */
    private DatagramSocket channel;

    /** The DatagramPacket of the current request. */
    private DatagramPacket current_packet;

    /** The direction that the elevator is currently moving in. */
    private Direction direction;

    /** The length of the buffer for receiving UDP packets in bytes. */
    private static final int BUFFER_LEN = 100;

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
        this.floor = 1; // Assume all elevators start on the ground floor
        this.direction = Direction.Up; // Can only move since on ground floor
        this.state = ElevatorState.Idle; // Elevators start in the idle state
        this.floor_q = new TreeSet<>();
        this.current_request = null;
        ELEVATOR_COUNT++;
    }

    /**
     * Moves the elevator to the destination floor.
     *
     * @param destination The destination floor number to move to.
     */
    private void moveTo(int destination) {
        // Already there
        if (destination == this.floor) {
            this.state = ElevatorState.Idle;
            return;
        }

        System.out.println("Elevator #" + this.id + " moving from floor " + this.floor + " to " + destination);

        // Send update on elevator location
        ElevatorRequest status = new ElevatorRequest(this.id, this.floor, destination);
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

        // Sleep 50ms per floor traversed
        try {
            Thread.sleep(50 * Math.abs(destination - this.floor));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        this.floor = destination;
    }

    /**
     * Opens the elevator doors.
     */
    private void openDoors() {
        System.out.println("Elevator #" + this.id + " opening doors.");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Closes the elevator doors.
     */
    private void closeDoors() {
        System.out.println("Elevator #" + this.id + " closing doors.");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Gets the next floor to move to according to the floors that need to be
     * visited and the elevator's current direction.
     * 
     * @return The next floor that the elevator should move to; null if there is no
     *         floor in the direction the elevator
     *         is moving.
     */
    private Integer nextFloor() {
        switch (this.direction) {
            case Direction.Up:
                return this.floor_q.higher(this.floor);
            case Direction.Down:
                return this.floor_q.lower(this.floor);
            default:
                return null;
        }
    }

    /**
     * Swaps the elevator's direction to the opposite.
     */
    private void toggleDirection() {
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
     * Implements the finite state machine logic of an elevator.
     */
    public void run() {

        while (true) {

            switch (this.state) {

                case ElevatorState.Idle:

                    // If there are still floors to visit, don't wait for new requests longer than
                    // 50ms
                    // This will still get new requests for handling multiple at once, but it won't
                    // wait too long
                    try {
                        if (!this.floor_q.isEmpty()) {
                            channel.setSoTimeout(50);
                        } else {
                            channel.setSoTimeout(0);
                        }
                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    }

                    // Construct a DatagramPacket for receiving packets up to 100 bytes long (the
                    // length of the byte array).
                    this.current_packet = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
                    System.out.println("Elevator " + this.id + ": Waiting for new elevator request...\n");

                    // Receive the packet from the scheduler
                    try {
                        channel.receive(this.current_packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

                    // Parse packet
                    try {
                        this.current_request = new ElevatorRequest(this.current_packet.getData());
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    // Add floors to be processed
                    floor_q.add(this.current_request.getOriginFloor());
                    floor_q.add(this.current_request.getDestinationFloor());
                    break;

                case ElevatorState.Moving:

                    // If there are no floors in our direction, then go the other way
                    if (this.nextFloor() == null) {
                        this.toggleDirection();
                    }

                    this.moveTo(this.nextFloor());
                    this.state = ElevatorState.DoorsOpen;
                    break;

                case ElevatorState.DoorsOpen:
                    this.openDoors();
                    this.state = ElevatorState.DoorsClosed;
                    break;

                case ElevatorState.DoorsClosed:
                    this.closeDoors();

                    // More floors to move to
                    if (!this.floor_q.isEmpty()) {
                        this.state = ElevatorState.Moving;
                        break;
                    }

                    // Echo back request marked complete
                    try {
                        this.current_request.markComplete();
                        this.current_packet.setData(this.current_request.getBytes());
                        this.current_packet.setPort(ElevatorSubsystem.PORT);
                        channel.send(this.current_packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

                    // Go to idle state to get more requests
                    this.state = ElevatorState.Idle;
                    break;
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

}
