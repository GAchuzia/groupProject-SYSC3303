public class ElevatorSubsystem implements Runnable {

    /** Queue to read incoming messages from. */
    private MessageQueue<ElevatorRequest> incoming;

    /** Queue to put outgoing messages to. */
    private MessageQueue<ElevatorRequest> outgoing;

    public ElevatorSubsystem(MessageQueue<ElevatorRequest> incoming, MessageQueue<ElevatorRequest> outgoing) {
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    public void run() {
        while (true) {
            ElevatorRequest request = this.incoming.getMessage();
            System.out.println("Elevator got request: " + request);
            System.out.println("Elevator echoed request back.");
            this.outgoing.putMessage(request);
        }
    }
}
