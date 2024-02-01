import java.io.FileNotFoundException;

/**
 * The main file of the program.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class Main {
    public static void main(String[] args) {

        MessageQueue<ElevatorRequest> floorIncoming = new MessageQueue<ElevatorRequest>();
        MessageQueue<ElevatorRequest> floorOutgoing = new MessageQueue<ElevatorRequest>();
        MessageQueue<ElevatorRequest> elevatorIncoming = new MessageQueue<ElevatorRequest>();
        MessageQueue<ElevatorRequest> elevatorOutgoing = new MessageQueue<ElevatorRequest>();

        FloorSubsystem fsys;
        try {
            fsys = new FloorSubsystem("./testdata.txt", floorIncoming, floorOutgoing);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file.");
            return;
        }
        fsys.run();

        while (!floorOutgoing.isEmpty()) {
            System.out.println("Got message on queue: " + floorOutgoing.getMessage());
        }
    }
}
