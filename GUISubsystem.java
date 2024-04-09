import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.IOException;
import java.net.SocketException;
import javax.swing.*;

/**
 * The scheduler that will be responsible for assigning the correct elevators to
 * the correct floor.
 * 
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class GUISubsystem {

    /** The port for receiving messages. */
    public static final int PORT = 2004;

    /** Buffer length for receiving UDP packets. */
    private static final int BUFFER_LEN = 100;

    /** Primary logic for the WebGUI. */
    public static void main(String[] args) throws SocketException, IOException {

        // Channel for receiving UDP messages from the scheduler.
        DatagramSocket channel = new DatagramSocket(PORT);

        // Show the GUI
        ElevatorPanel elevatorPanel = createGUI();

        // Update the GUI as packets are received
        while (true) {

            // Wait for status update
            ElevatorStatus statusUpdate = receiveUpdate(channel);

            // Display status update
            elevatorPanel.updateDisplay(statusUpdate);
        }
    }

    /**
     * Creates and displays the GUI.
     * 
     * @return A reference to the ElevatorPanel so it can be updated with new
     *         information.
     */
    public static ElevatorPanel createGUI() {
        JFrame frame = new JFrame("Elevator GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ElevatorPanel elevatorPanel = new ElevatorPanel();
        frame.add(elevatorPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return elevatorPanel;
    }

    /**
     * Waits for an elevator status update from the scheduler.
     * 
     * @param channel The UDP socket to use for receiving messages.
     * @return The received ElevatorStatus object.
     */
    public static ElevatorStatus receiveUpdate(DatagramSocket channel) throws IOException {
        DatagramPacket p = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
        channel.receive(p);
        return new ElevatorStatus(p.getData());
    }
}
