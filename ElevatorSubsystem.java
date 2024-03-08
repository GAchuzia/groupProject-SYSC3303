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
    public static final int NUM_ELEVATORS = 1;

    /** Runs the primary logic of the ElevatorSubsystem. */
    public static void main(String[] args) throws IOException, SocketException {

        // Create socket for receiving requests
        DatagramSocket channel = new DatagramSocket(PORT);

        // Create outgoing message queue for elevators TODO: make this UDP
        MessageQueue<ElevatorRequest> outgoing = new MessageQueue<>();

        // Initialize all elevators
        Thread[] elevators = new Thread[NUM_ELEVATORS];
        ArrayList<MessageQueue<ElevatorRequest>> elevators_in = new ArrayList<>();
        for (int i = 0; i < NUM_ELEVATORS; i++) {
            elevators_in.add(new MessageQueue<>());
            elevators[i] = new Thread(new Elevator(elevators_in.get(i), outgoing));
        }

        // Start all elevators
        for (int i = 0; i < elevators.length; i++) {
            elevators[i].start();
        }

        // Process requests from scheduler
        DatagramPacket message = null;
        while (true) {

            message = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
            channel.receive(message);
            ElevatorRequest request = new ElevatorRequest(message.getData());

            // Handle request (forward it to the only elevator that currently exists)
            // WARNING: assumes a single elevator
            elevators_in.get(0).putMessage(request);

            // TODO: Read back messages from the elevators
        }
    }
}
