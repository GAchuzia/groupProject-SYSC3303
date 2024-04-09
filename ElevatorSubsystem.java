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

        Elevator[] elevators = initElevators();
        Thread[] elevator_threads = startElevators(elevators);

        // Process requests from scheduler
        while (true) {

            DatagramPacket message = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
            channel.receive(message);

            // Forward message
            if (message.getPort() == Scheduler.PORT) {
                ElevatorRequest request = new ElevatorRequest(message.getData());
                message.setPort(2007 + request.getElevator()); // Set destination port to correct elevator
                channel.send(message);
            }

            // Message came from an elevator and it's a status update
            else {
                message.setPort(Scheduler.PORT);
                channel.send(message);
            }
        }
    }

    /**
     * Initializes `NUM_ELEVATORS` elevators and returns an array containing them
     * all.
     * 
     * @return An array containing all the initialized elevators.
     */
    public static Elevator[] initElevators() throws SocketException {
        Elevator[] elevators = new Elevator[NUM_ELEVATORS];
        for (int i = 0; i < NUM_ELEVATORS; i++) {
            elevators[i] = new Elevator(ELEVATOR_PORT_START + i);
        }
        return elevators;
    }

    /**
     * Creates a thread for each elevator and starts it running.
     *
     * @param elevators An array of initialized Elevators to be run.
     * @return An array of the elevator threads.
     */
    public static Thread[] startElevators(Elevator[] elevators) {
        Thread[] threads = new Thread[elevators.length];
        for (int i = 0; i < elevators.length; i++) {
            threads[i] = new Thread(elevators[i]);
            threads[i].start();
        }
        return threads;
    }
}
