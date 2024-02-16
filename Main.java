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

        // Construct message queues
        MessageQueue<ElevatorRequest> floorIncoming = new MessageQueue<>();
        MessageQueue<ElevatorRequest> floorOutgoing = new MessageQueue<>();
        MessageQueue<ElevatorRequest> elevatorIncoming = new MessageQueue<>();
        MessageQueue<ElevatorRequest> elevatorOutgoing = new MessageQueue<>();

        // Construct subsystems
        ElevatorSubsystem esys = new ElevatorSubsystem(elevatorIncoming, elevatorOutgoing);
        Scheduler scheduler = new Scheduler(floorIncoming, floorOutgoing, elevatorIncoming, elevatorOutgoing);
        FloorSubsystem fsys;
        try {
            fsys = new FloorSubsystem("./testdata.txt", floorIncoming, floorOutgoing);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file.");
            return;
        }

        // Run subsystems in separate threads
        new Thread(scheduler, "Scheduler").start(); // Put this last so it doesn't quit immediately
        new Thread(fsys, "Floors").start();
        new Thread(esys, "Elevators").start();
    }
}
