import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;

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
public class Elevator_V3 implements Runnable {

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

//    /**
//     * The message queue for the elevator to receive requests from the
//     * ElevatorSubystem.
//     */
//    private MessageQueue<ElevatorRequest> request_q;

//    /**
//     * The message queue for the elevator to send completion messages back out to
//     * the ElevatorSubystem.
//     */
//    private MessageQueue<ElevatorRequest> completion_q;

    /**
     * Keeps track of the floors the elevator needs to visit.
     */
    private ArrayList<Integer> floor_q;

    /**
     * The current request being handled by the Elevator.
     */
    private ElevatorRequest current_request;

    ElevatorRequest schedulerMessage;



    /**
     * packets to store data
     */
    DatagramPacket sendPacket, receivePacket;
    /**
     * used to both send and receive information
     */
    DatagramSocket sendReceiveSocket;

    Integer socketPort;

    /**
     * Constructs a new elevator.
     *
     * @param socketPort The Port at which we will be communicating to the elevator with

     */
    public Elevator_V3(Integer socketPort) {
        try {
            // Construct a datagram socket and bind it to the
            // port passed in the constructor
            sendReceiveSocket = new DatagramSocket(socketPort);
        } catch (SocketException se) {   // Can't create the socket.
            se.printStackTrace();
            System.exit(1);
        }



        //this.request_q = requests;
        //this.completion_q = completion;
        this.socketPort = socketPort;
        this.id = ELEVATOR_COUNT;
        this.floor = 1; // Assume all elevators start on the ground floor
        this.state = ElevatorState.Idle; // Elevators start in the idle state
        this.floor_q = new ArrayList<>();
        this.schedulerMessage = null;
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
     * Implements the finite state machine logic of an elevator.
     */
    public void run() {

        while (true) {
            // Construct a DatagramPacket for receiving packets up
            // to 100 bytes long (the length of the byte array).
            byte clientData[] = new byte[100];
            receivePacket = new DatagramPacket(clientData, clientData.length);
            System.out.println("Elevator " + this.id + ": Waiting for new elevator request...\n");

            //receive the packet from the scheduler
            try {
                sendReceiveSocket.receive(receivePacket);
            } catch (IOException e) {
                System.out.print("IO Exception: likely:");
                System.out.println("Receive Socket Timed Out.\n" + e);
                e.printStackTrace();
                System.exit(1);
            }



            switch (this.state) {
                case ElevatorState.Idle:

                    if (this.floor_q.isEmpty()) {
                        if (this.receivePacket.getData() == null) {
                            return;
                        }

                        ElevatorRequest schedulerMessage = null;
                        try {
                            schedulerMessage = new ElevatorRequest(this.receivePacket.getData());
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        //this.current_request = this.request_q.getMessage(); // Get any incoming request
                        this.current_request = schedulerMessage; // Get any incoming request


                        // Add floors to be processed
                        floor_q.add(this.current_request.getOriginFloor());
                        floor_q.add(this.current_request.getDestinationFloor());
                    }

                    this.state = ElevatorState.Moving;

                    break;
                case ElevatorState.Moving:
                    this.moveTo(floor_q.removeFirst()); // Go to the origin floor for pickup
                    this.state = ElevatorState.DoorsOpen;
                    break;
                case ElevatorState.DoorsOpen:
                    this.openDoors(); // Let in passenger
                    this.state = ElevatorState.DoorsClosed;
                    break;
                case ElevatorState.DoorsClosed:
                    this.closeDoors(); // Get ready to leave

                    if (this.floor_q.isEmpty()) {
                        this.state = ElevatorState.Idle; // Needs new request

                        try {
                            sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(),
                                    InetAddress.getLocalHost(), socketPort);
                        }catch (UnknownHostException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }

                        try {
                            sendReceiveSocket.send(sendPacket); // Echo back request to signify completion
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }

                        //this.completion_q.putMessage(this.current_request); // Echo back request to signify completion
                    } else {
                        this.state = ElevatorState.Moving; // More floors to move to
                    }

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

}
