import java.net.DatagramSocket;

/**
 * The scheduler that will be responsible for assigning the correct elevators to
 * the correct floor.
 * 
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */

public class Scheduler {

    /** The current state of the Scheduler (starts in Idle). */
    static SchedulerState state = SchedulerState.Idle;

    /** The port for sending and receiving from the floor subsystem. */
    static int FLOOR_PORT = 2003;

    /** The port for sending and receiving from the elevator system. */
    static int ELEVATOR_PORT = 2004;

    /** Executes the main logical loop of the Scheduler subsystem. */
    public static void main(String[] args) {

        // Create socket for receiving and sending
        DatagramSocket floor_socket = new DatagramSocket(FLOOR_PORT);
        DatagramSocket elevator_socket = new DatagramSocket(ELEVATOR_PORT);

        // While there are still messages
        while (true) {
            switch (state) {
                case SchedulerState.Idle:
                    // TODO: Get UDP messages
                    state = SchedulerState.Thinking;
                    break;
                case SchedulerState.Thinking:

                    // If there is a message from the floor, forward it to the elevator subsystem
                    if (!this.floorOutgoing.isEmpty()) {
                        state = SchedulerState.Thinking;
                        System.out.println("Scheduler forwarded floor message.");
                        this.elevatorIncoming.putMessage(this.floorOutgoing.getMessage());
                    }

                    // If there is a message from the elevator subsystem, forward it to the floor
                    if (!this.elevatorOutgoing.isEmpty()) {
                        state = SchedulerState.Thinking;
                        System.out.println("Scheduler forwarded elevator message.");

                        ElevatorRequest message = this.elevatorOutgoing.getMessage();
                        if (message == null) {
                            this.floorIncoming.putMessage(null);
                            System.out.println("Scheduler exited.");
                            return;
                        }
                        this.floorIncoming.putMessage(message);
                    }
                    state = SchedulerState.Idle;
                    break;
            }

            // Add some delay
            try {
                Thread.sleep(4);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}

/**
 * Describes the current state of the Scheduler.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
enum SchedulerState {
    Thinking,
    Idle,
}
