import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.SocketException;

/**
 * Represents the subsystem containing the elevators.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class ElevatorSubsystem {

    /** Port for sending and receiving messages. */
    public static final int PORT = 2003;

    /** Length of buffer in bytes for receiving UDP messages. */
    public static final int BUFFER_LEN = 100;

    /** Number of elevators in the simulation. */
    public static final int NUM_ELEVATORS = 4;

    /**
     * The port number at which elevator ports begin. Elevator ports are
     * ELEVATOR_PORT_START + the elevator ID.
     */
    public static final int ELEVATOR_PORT_START = 2007;

    /** Runs the primary logic of the ElevatorSubsystem. */
    public static void main(String[] args) throws IOException, SocketException {

        // Create socket for receiving requests
        DatagramSocket channel = new DatagramSocket(PORT);

        // Initialize all elevators
        Thread[] elevators = new Thread[NUM_ELEVATORS];
        for (int i = 0; i < NUM_ELEVATORS; i++) {
            elevators[i] = new Thread(new Elevator(ELEVATOR_PORT_START + i));
        }

        // Start all elevators
        for (int i = 0; i < elevators.length; i++) {
            elevators[i].start();
        }

        // Process requests from scheduler
        while (true) {

            DatagramPacket message = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
            channel.receive(message);
            System.out.println("RECEIVED!");
            ElevatorRequest request = new ElevatorRequest(message.getData());

            // Forward message
            message.setPort(2007 + request.getElevator()); // Set destination port to correct elevator
            channel.send(message);

            // TODO: Read back messages from the elevators
        }
    }
}
