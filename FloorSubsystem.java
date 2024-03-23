import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.io.IOException;

/**
 * This class represents the floor subsystem, which will read the elevator usage
 * data.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class FloorSubsystem {

    static final int PORT = 2001;

    static final int BUFFER_LEN = 100;

    static final String INPUT_FILE = "./testdata.txt";

    /** Runs the primary logic of the FloorSubsystem. */
    public static void main(String[] args) throws FileNotFoundException, SocketException, IOException {

        // Create socket for sending and receiving.
        DatagramSocket channel = new DatagramSocket(PORT);

        // The message buffer for receiving new UDP messages
        DatagramPacket message = null;

        // Reads the input file.
        File file = new File(INPUT_FILE);
        Scanner reader = new Scanner(file);

        // Send all messages
        while (reader.hasNextLine()) {
            try {

                ElevatorRequest rqst = new ElevatorRequest(reader.nextLine());
                System.out.println("Floor put request on queue: " + rqst);
                byte[] byte_rqst = rqst.getBytes();
                channel.send(
                        new DatagramPacket(byte_rqst, byte_rqst.length, InetAddress.getLocalHost(), Scheduler.PORT));

                // Short sleep to delay requests
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    continue;
                }

            } catch (DateTimeParseException e) {
                System.out.println("Failed to parse input line timestamp: " + e);
            }
        }
        reader.close();

        // Look for incoming messages
        while (true) {

            message = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
            channel.receive(message);
            ElevatorRequest response = new ElevatorRequest(message.getData());
            System.out.println("Floor got message: " + response);
        }
    }
}
