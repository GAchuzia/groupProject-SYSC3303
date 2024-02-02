import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

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
public class FloorSubsystem implements Runnable {

    /** Reads the input file. */
    private Scanner reader;

    /** Queue to put elevator request messages on. */
    private MessageQueue<ElevatorRequest> incoming;

    /** Queue to read messages from. */
    private MessageQueue<ElevatorRequest> outgoing;

    /**
     * Constructs the floor subsystem.
     * 
     * @param file_path The file to read the input requests from for the simulation.
     * @param incoming  The message queue with incoming messages for the
     *                  FloorSubsystem.
     * @param outgoing  The message queue with outgoing messages from the
     *                  FloorSubsystem.
     */
    public FloorSubsystem(String file_path, MessageQueue<ElevatorRequest> incoming,
            MessageQueue<ElevatorRequest> outgoing) throws FileNotFoundException {
        File file = new File(file_path);
        this.reader = new Scanner(file);
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    /** Runs the primary logic of the FloorSubsystem. */
    public void run() {

        // Send all messages
        while (this.reader.hasNextLine()) {
            try {
                ElevatorRequest rqst = new ElevatorRequest(this.reader.nextLine());
                System.out.println("Floor put request on queue: " + rqst);
                this.outgoing.putMessage(rqst);
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    continue;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Failed to parse input line timestamp: " + e);
            }
        }
        this.reader.close();

        // Look for incoming messages
        while (true) {
            while (!this.incoming.isEmpty()) {
                System.out.println("Floor got message: " + this.incoming.getMessage());
            }
        }
    }
}
