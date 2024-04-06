import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
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

    /** Port number for receiving UDP packets. */
    static final int PORT = 2001;

    /** Byte buffer length for receiving UDP packets. */
    static final int BUFFER_LEN = 100;

    /** The number of floors in the building. */
    public static final int NUM_FLOORS = 20;

    /** The ground floor of the building. */
    public static final int GROUND_FLOOR = 1;

    /** Time between each request in milliseconds. */
    static final int TIME_BETWEEN_REQUESTS = 5000;

    /** Runs the primary logic of the FloorSubsystem. */
    public static void main(String[] args) throws FileNotFoundException, SocketException, IOException {

        // Create socket for sending and receiving.
        DatagramSocket channel = new DatagramSocket(PORT);

        // The message buffer for receiving new UDP messages
        DatagramPacket message = null;

        // Reads the input file.
        File file = new File(args[0]);
        Scanner reader = new Scanner(file);

        // Periodically read input file to simulate time between requests
        Timer requester = new Timer();
        requester.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    nextRequest(reader, channel);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }, 0, TIME_BETWEEN_REQUESTS);

        // Continually check for completed messages and print them
        while (true) {
            message = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
            channel.receive(message);
            ElevatorRequest response = new ElevatorRequest(message.getData());
            System.out.println("Floor got message: " + response);

        }
    }

    public static void nextRequest(Scanner reader, DatagramSocket socket) throws IOException {
        if (reader.hasNextLine()) {
            try {
                ElevatorRequest rqst = new ElevatorRequest(reader.nextLine());
                System.out.println("Floor put request on queue: " + rqst);
                byte[] byte_rqst = rqst.getBytes();
                socket.send(new DatagramPacket(byte_rqst, byte_rqst.length, InetAddress.getLocalHost(),
                        Scheduler.PORT));
            } catch (DateTimeParseException e) {
                System.out.println("Failed to parse input line timestamp: " + e);
            }
        }
    }
}
