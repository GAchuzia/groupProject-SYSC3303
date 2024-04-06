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
    static final int PORT = 2004;

    /** Buffer length for receiving UDP packets. */
    static final int BUFFER_LEN = 100;

    /** Primary logic for the WebGUI. */
    public static void main(String[] args) throws SocketException, IOException {
        DatagramSocket channel = new DatagramSocket(PORT);

        JFrame frame = new JFrame("Elevator GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ElevatorPanel elevatorPanel = new ElevatorPanel();
        frame.add(elevatorPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (true) {

            // Wait for packet from scheduler
            DatagramPacket p = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
            channel.receive(p);

            // Parse packet
            ElevatorStatus status = new ElevatorStatus(p.getData());

            // Display
            elevatorPanel.updateDisplay(status);
        }
    }

}
