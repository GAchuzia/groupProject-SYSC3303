import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.io.IOException;

/**
 * Represents a scheduler for an elevator system, responsible for assigning
 * elevators to requests based on their
 * current state and location. It listens for UDP messages from both floor and
 * elevator subsystems, routing requests
 * from floors to elevators and sending elevator status updates to the GUI. The
 * scheduler operates in a loop, handling
 * messages according to its current state.
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
    private static SchedulerState state = SchedulerState.Idle;

    /** The port for sending and receiving messages. */
    public static final int PORT = 2002;

    /** The length of the buffer for receiving UDP messages. */
    private static final int BUFFER_LEN = 100;

    /** The floor number where the request originated. */
    private static int originFloor;

    /**
     * Maintains the status of each elevator in the system, including its current
     * floor,
     * direction, and whether it is in service.
     */
    private static ElevatorStatus[] statuses = new ElevatorStatus[ElevatorSubsystem.NUM_ELEVATORS];

    /** Executes the main logical loop of the Scheduler subsystem. */
    /**
     * The main entry point of the scheduler application. It sets up a
     * DatagramSocket for
     * communication and continuously listens for messages from floor and elevator
     * subsystems,
     * processing each according to the scheduler's current state.
     *
     * @param args Command line arguments (not used).
     * @throws SocketException If a socket could not be opened, or the socket could
     *                         not bind to the specified port.
     * @throws IOException     If an I/O error occurs.
     */
    public static void main(String[] args) throws SocketException, IOException {

        // Create socket for receiving and sending
        DatagramSocket channel = new DatagramSocket(PORT);

        // The message buffer for receiving new UDP messages
        DatagramPacket message = null;

        // Pre-fill the elevator list with current floor values (ground floor).
        for (int i = 0; i < statuses.length; i++) {
            statuses[i] = new ElevatorStatus();
        }

        // While there are still messages
        while (true) {
            switch (state) {

                case SchedulerState.Idle:
                    message = new DatagramPacket(new byte[BUFFER_LEN], BUFFER_LEN);
                    channel.receive(message);
                    state = SchedulerState.Thinking;
                    break;

                case SchedulerState.Thinking:

                    switch (message.getPort()) {
                        // If there is a message from the floor, forward it to the elevator subsystem
                        case FloorSubsystem.PORT:
                            state = SchedulerState.Thinking;

                            // Selects the nearest available elevator with the correct direction
                            ElevatorRequest request = new ElevatorRequest(message.getData());

                            // Route the request
                            int chosenElevator = selectElevator(statuses, request);
                            request.setElevator(chosenElevator);
                            message.setData(request.getBytes()); // Re-encode message
                            message.setPort(ElevatorSubsystem.PORT);
                            channel.send(message);

                            System.out.println(
                                    "Scheduler forwarded request [" + request + "] to elevator "
                                            + request.getElevator());
                            // Set state back to idle
                            state = SchedulerState.Idle;
                            break;

                        // If there is a message from the elevator subsystem, forward it to the floor
                        case ElevatorSubsystem.PORT:
                            state = SchedulerState.Thinking;

                            ElevatorRequest response = new ElevatorRequest(message.getData());

                            // It's a status update, so record elevator information
                            int i = response.getElevator();
                            statuses[i].setElevator(i);
                            statuses[i].setFloor(response.getOriginFloor());
                            statuses[i].setDirection(response.getDirection());
                            statuses[i].setRiders(response.getRiders());
                            statuses[i].setDoor(response.getDoor());
                            statuses[i].setDestinationFloor(response.getFinalDestinationFloor());
                            statuses[i].setOriginFloor(response.getInitialOriginFloor());
                            statuses[i].setComplete(response.isFinalComplete());

                            // Set state back to idle as a default option
                            state = SchedulerState.Idle;

                            // Check if the elevator is shutting down
                            if (response.getTimerFault()) {
                                statuses[response.getElevator()].markShutDown();
                                System.out.println(
                                        "Scheduler notified that elevator " + response.getElevator() + " shut down.");
                                System.out.println("Re-assigning request to new elevator.");

                                // Trick state machine into thinking this is a new request
                                response.setTimerFault(false);
                                message.setData(response.getBytes());
                                message.setPort(FloorSubsystem.PORT);
                                state = SchedulerState.Thinking; // Override state back to thinking
                            }

                            // Set the message destination to the Floor Subsystem to notify completion.
                            if (response.isComplete()) {
                                message.setPort(FloorSubsystem.PORT);
                                channel.send(message); // Forward the completion message to the Floor Subsystem.
                                System.out.println("Scheduler forwarded elevator message to floor.");
                                // Set state back to idle
                                state = SchedulerState.Idle;
                                break;
                            }

                            // Forward the status update to the GUI
                            byte[] data = statuses[i].getBytes();
                            DatagramPacket update = new DatagramPacket(data, data.length);
                            update.setPort(GUISubsystem.PORT);
                            update.setAddress(message.getAddress());
                            channel.send(update);

                            break;
                    }
            }
        }
    }

    /**
     * Selects the most appropriate elevator to handle a request based on the
     * current status of all elevators
     * and the request details. The method considers the direction of elevator
     * movement and its current floor
     * to minimize wait times and improve efficiency.
     *
     * @param request The elevator request including the origin floor and desired
     *                direction.
     * @return The ID of the selected elevator to handle the request.
     */
    public static int selectElevator(ElevatorStatus statuses[], ElevatorRequest request) {

        // Schedule based on pick-up location only
        originFloor = request.getOriginFloor();

        int chosenElevator = 0; // The elevator selected for this request (by default pick the first one)

        for (int i = 0; i < statuses.length; i++) {

            // Ignore shut-down elevators or elevators at capacity
            if (statuses[i].isShutDown() || statuses[i].getRiders() >= Elevator.CAPACITY_LIMIT) {
                continue;
            }

            if (statuses[i].getDirection() == Direction.Up && (statuses[i].getFloor() <= originFloor)) {
                chosenElevator = i;
                break;
            } else if (statuses[i].getDirection() == Direction.Down && (statuses[i].getFloor() >= originFloor)) {
                chosenElevator = i;
                break;
            }
        }

        // If we picked the default option (0), ensure that it's not shut down.
        // Otherwise, pick the next elevator that
        // hasn't been shut down
        if (chosenElevator == 0 && statuses[0].isShutDown()) {
            for (int i = 0; i < statuses.length; i++) {
                if (!statuses[i].isShutDown()) {
                    chosenElevator = i;
                    break;
                }
            }
        }

        return chosenElevator;
    }
}

/**
 * Enumerates the possible states of the Scheduler. The scheduler's state
 * determines how it reacts to incoming
 * messages and its overall behavior at any given time.
 * <ul>
 * <li>Idle - The scheduler is waiting for new messages.</li>
 * <li>Thinking - The scheduler is processing an incoming message and
 * determining the appropriate action.</li>
 * </ul>
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
