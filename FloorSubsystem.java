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

    /**
     * Reads the input file.
     */
    private Scanner reader;

    /** Queue to put elevator request messages on. */
    private MessageQueue<ElevatorRequest> queue;

    public FloorSubsystem(String file_path, MessageQueue<ElevatorRequest> queue) throws FileNotFoundException {
        File file = new File(file_path);
        this.reader = new Scanner(file);
        this.queue = queue;
    }

    public void run() {
        while (this.reader.hasNextLine()) {
            try {
                ElevatorRequest rqst = new ElevatorRequest(this.reader.nextLine());
                System.out.println("Put request on queue: " + rqst);
                this.queue.putMessage(rqst);
            } catch (DateTimeParseException e) {
                System.out.println("Failed to parse input line timestamp: " + e);
            }
        }
        this.reader.close();
    }
}
