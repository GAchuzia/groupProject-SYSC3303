/**
 * Represents a physical or simulated elevator.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class Elevator implements Runnable {

    /** Counts the number of elevators that have been created. */
    static private int ELEVATOR_COUNT = 0;

    /** The unique identifier of this elevator. */
    private final int id;

    /** The floor that the elevator is currently on. */
    private int floor;

    /** The current state of the elevator. */
    private ElevatorState state;

    /**
     * The message queue for the elevator to receive requests from the
     * ElevatorSubystem.
     */
    private MessageQueue<ElevatorRequest> request_q;

    /**
     * The message queue for the elevator to send completion messages back out to
     * the ElevatorSubystem.
     */
    private MessageQueue<ElevatorRequest> completion_q;

    /**
     * Constructs a new elevator.
     *
     * @param requests   The message queue for the elevator to receive requests on.
     * @param completion The message queue for the elevator to notify the monitoring
     *                   system of its task completion.
     */
    public Elevator(MessageQueue<ElevatorRequest> requests, MessageQueue<ElevatorRequest> completion) {
        this.request_q = requests;
        this.completion_q = completion;
        this.id = ELEVATOR_COUNT;
        this.floor = 1; // Assume all elevators start on the ground floor
        this.state = ElevatorState.Idle; // Elevators start in the idle state
        ELEVATOR_COUNT++;
    }

    /**
     * Moves the elevator to the destination floor.
     * 
     * @param destination The destination floor number to move to.
     */
    private void moveTo(int destination) {
        // Already there
        if (destination == this.floor) {
            this.state = ElevatorState.Idle;
            return;
        }

        this.state = ElevatorState.Moving; // Signify movement
        System.out.println("Elevator #" + this.id + " moving from floor " + this.floor + " to " + destination);

        // Sleep 50ms per floor traversed
        try {
            Thread.sleep(50 * Math.abs(destination - this.floor));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        this.state = ElevatorState.Idle;
        this.floor = destination;
    }

    /**
     * Opens the elevator doors.
     */
    private void openDoors() {
        System.out.println("Elevator #" + this.id + " opening doors.");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.state = ElevatorState.DoorsOpen;
    }

    /**
     * Closes the elevator doors.
     */
    private void closeDoors() {
        System.out.println("Elevator #" + this.id + " closing doors.");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.state = ElevatorState.DoorsClosed;
    }

    /**
     * Implements the finite state machine logic of an elevator.
     */
    public void run() {
        while (true) {
            ElevatorRequest rqst = this.request_q.getMessage(); // Get any incoming request

            // Handle kill message
            if (rqst == null) {
                return;
            }

            this.moveTo(rqst.getOriginFloor()); // Go to the origin floor for pickup
            this.openDoors(); // Let in passenger
            this.closeDoors(); // Get ready to leave
            this.moveTo(rqst.getDestinationFloor()); // Go to destination
            this.openDoors(); // Let out passenger
            this.closeDoors(); // Get ready to leave

            this.completion_q.putMessage(rqst); // Echo back request to signify completion
        }
    }

}
