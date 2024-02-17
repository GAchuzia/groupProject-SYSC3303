import java.util.ArrayList;

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
public class ElevatorSubsystem implements Runnable {

    /** Queue to read incoming messages from. */
    private MessageQueue<ElevatorRequest> incoming;

    /** Queue to put outgoing messages to. */
    private MessageQueue<ElevatorRequest> outgoing;

    /** A list of all the elevators controlled by this subsystem. */
    private Thread[] elevators;

    /**
     * A list of all the message queues that the elevators can receives instructions
     * on.
     */
    private ArrayList<MessageQueue<ElevatorRequest>> elevators_in;

    /**
     * Constructs the elevator subsystem.
     * 
     * @param incoming      The message queue containing messages for the
     *                      ElevatorSubsystem.
     * @param outgoing      The message queue containing messages from the
     *                      ElevatorSubsystem.
     * @param num_elevators The number of elevators that this system controls.
     */
    public ElevatorSubsystem(MessageQueue<ElevatorRequest> incoming, MessageQueue<ElevatorRequest> outgoing,
            int num_elevators) {
        this.incoming = incoming;
        this.outgoing = outgoing;

        // Initialize all elevators
        this.elevators = new Thread[num_elevators];
        this.elevators_in = new ArrayList<>();
        for (int i = 0; i < num_elevators; i++) {
            this.elevators_in.add(new MessageQueue<>());
            this.elevators[i] = new Thread(new Elevator(this.elevators_in.get(i), outgoing));
        }

    }

    /** Runs the primary logic of the ElevatorSubsystem. */
    public void run() {

        // Start all elevators
        for (int i = 0; i < this.elevators.length; i++) {
            this.elevators[i].start();
        }

        while (true) {
            ElevatorRequest request = this.incoming.getMessage();

            // Destroy subystem
            if (request == null) {
                System.out.println("Elevator system exited.");

                // Kill all elevator threads
                for (int i = 0; i < this.elevators.length; i++) {
                    this.elevators_in.get(i).putMessage(null);
                }

                this.outgoing.putMessage(null); // Signal to other subsystems that it's over
                return;
            }

            // Handle request (forward it to the only elevator that currently exists)
            // WARNING: assumes a single elevator
            this.elevators_in.get(0).putMessage(request);
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}
