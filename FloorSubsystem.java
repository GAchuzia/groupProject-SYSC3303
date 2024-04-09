import java.io.File;
import java.io.FileNotFoundException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Stream;
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
    public static final int NUM_FLOORS = 22;

    /** The ground floor of the building. */
    public static final int GROUND_FLOOR = 1;

    /** The file to read requests from. */
    public static final String DATA_FILE = "testdata.txt";

    /** Time between each request in milliseconds. */
    static final int TIME_BETWEEN_REQUESTS = 5000;

    /** Runs the primary logic of the FloorSubsystem. */
    public static void main(String[] args) throws FileNotFoundException, SocketException, IOException {

        // Create socket for sending and receiving.
        DatagramSocket channel = new DatagramSocket(PORT);

        // Reads the input file.
        File file = new File(DATA_FILE);
        Scanner reader = new Scanner(file);

        // Track the number of sent requests and total requests
        long numRequests = 0;
        long receivedRequests = 0;
        try (Stream<String> fileStream = Files.lines(Paths.get(DATA_FILE))) {
            numRequests = fileStream.count();
        }

        // Periodically read input file to simulate time between requests
        Timer requester = new Timer();
        requester.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    ElevatorRequest rqst = nextRequest(reader);
                    if (rqst == null) {
                        System.out.println("No more requests.");
                        this.cancel();
                        return; // Time to stop
                    }
                    sendRequest(rqst, channel);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }, 0, TIME_BETWEEN_REQUESTS);

        // Track current time of first request
        Date startTime = new Date();

        // Continually check for completed messages and print them
        while (true) {
            ElevatorRequest response = receiveProcessedRequest(channel);
            System.out.println("Floor got message: " + response);
            receivedRequests++;

            if (receivedRequests == numRequests) {
                Date finishTime = new Date();
                long totalTime = finishTime.getTime() - startTime.getTime();
                System.out.println("All requests completed in " + totalTime / 1000 + " seconds.");
                System.exit(0);
            }

        }
    }

    /**
     * Waits to receive a UDP message from the scheduler.
     * 
     * @param socket The UDP socket to receive from.
     * @return The received ElevatorRequest object.
     */
    public static ElevatorRequest receiveProcessedRequest(DatagramSocket socket) throws IOException {
        DatagramPacket message = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
        socket.receive(message);
        return new ElevatorRequest(message.getData());
    }

    /**
     * Reads the next elevator request from the input file.
     * 
     * @param reader The file reader that is reading the input request file.
     * @return The next elevator request as an ElevatorRequest object.
     */
    public static ElevatorRequest nextRequest(Scanner reader) {
        if (reader.hasNextLine()) {
            try {
                return new ElevatorRequest(reader.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Failed to parse input line timestamp: " + e);
            }
        }
        return null;
    }

    /**
     * Sends an elevator request to the scheduler over UDP.
     * 
     * @param request The request to be sent.
     * @param socket  The UDP socket to use to send the request.
     */
    public static void sendRequest(ElevatorRequest request, DatagramSocket socket) throws IOException {
        System.out.println("Floor put request on queue: " + request);
        byte[] byte_rqst = request.getBytes();
        socket.send(new DatagramPacket(byte_rqst, byte_rqst.length, InetAddress.getLocalHost(),
                Scheduler.PORT));
    }
}
