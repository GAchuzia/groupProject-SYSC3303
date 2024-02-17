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

public class Scheduler implements Runnable {

    private MessageQueue<ElevatorRequest> floorIncoming;
    private MessageQueue<ElevatorRequest> floorOutgoing;
    private MessageQueue<ElevatorRequest> elevatorIncoming;
    private MessageQueue<ElevatorRequest> elevatorOutgoing;

    private SchedulerState state;

    public Scheduler(MessageQueue<ElevatorRequest> floorIncoming, MessageQueue<ElevatorRequest> floorOutgoing,
            MessageQueue<ElevatorRequest> elevatorIncoming, MessageQueue<ElevatorRequest> elevatorOutgoing) {
        this.floorIncoming = floorIncoming;
        this.floorOutgoing = floorOutgoing;
        this.elevatorIncoming = elevatorIncoming;
        this.elevatorOutgoing = elevatorOutgoing;
        this.state = SchedulerState.Idle;
    }

    /** Executes the main logical loop of the Scheduler subsystem. */
    public void run() {

        // While there are still messages
        while (true) {
            this.state = SchedulerState.Idle;

            // If there is a message from the floor, forward it to the elevator subsystem
            if (!this.floorOutgoing.isEmpty()) {
                this.state = SchedulerState.Thinking;
                System.out.println("Scheduler forwarded floor message.");
                this.elevatorIncoming.putMessage(this.floorOutgoing.getMessage());
            }

            // If there is a message from the elevator subsystem, forward it to the floor
            if (!this.elevatorOutgoing.isEmpty()) {
                this.state = SchedulerState.Thinking;
                System.out.println("Scheduler forwarded elevator message.");

                ElevatorRequest message = this.elevatorOutgoing.getMessage();
                if (message == null) {
                    this.floorIncoming.putMessage(null);
                    System.out.println("Scheduler exited.");
                    return;
                }
                this.floorIncoming.putMessage(message);
            }
            this.state = SchedulerState.Idle;

            try {
                Thread.sleep(4);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}

enum SchedulerState {
    Thinking,
    Idle,
}
