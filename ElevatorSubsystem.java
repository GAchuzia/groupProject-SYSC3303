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

    /**
     * Constructs the elevator subsystem.
     * 
     * @param incoming The message queue containing messages for the
     *                 ElevatorSubsystem.
     * @param outgoing The message queue containing messages from the
     *                 ElevatorSubsystem.
     */
    public ElevatorSubsystem(MessageQueue<ElevatorRequest> incoming, MessageQueue<ElevatorRequest> outgoing) {
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    /** Runs the primary logic of the ElevatorSubsystem. */
    public void run() {
        while (true) {
            ElevatorRequest request = this.incoming.getMessage();

            if (request == null) {
                System.out.println("Elevator system exited.");
                this.outgoing.putMessage(null);
                return;
            }

            System.out.println("Elevator got request: " + request);
            System.out.println("Elevator echoed request back.");
            this.outgoing.putMessage(request);

            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}
