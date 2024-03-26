import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.abs;

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

    /** The port for sending and receiving messages. */
    static final int PORT = 2002;

    /** The length of the buffer for receiving UDP messages. */
    static final int BUFFER_LEN = 100;

    /** The floor number where the request originated. */
    static int originFloor;

    /** The number of the most recently used elevator reaching its destination. */
    static int elevatorNum;

    /**
     * A list tracking the current floors of all available elevators.
     */
    static ElevatorStatus[] statuses = new ElevatorStatus[ElevatorSubsystem.NUM_ELEVATORS];

    /** Executes the main logical loop of the Scheduler subsystem. */
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
                            int chosenElevator = selectElevator(request);
                            request.setElevator(chosenElevator);
                            message.setData(request.getBytes()); // Re-encode message
                            message.setPort(ElevatorSubsystem.PORT);
                            channel.send(message);

                            System.out
                                    .println("Scheduler forwarded floor message to elevator " + request.getElevator());
                            // Set state back to idle
                            state = SchedulerState.Idle;
                            break;

                        // If there is a message from the elevator subsystem, forward it to the floor
                        case ElevatorSubsystem.PORT:
                            state = SchedulerState.Thinking;

                            ElevatorRequest response = new ElevatorRequest(message.getData());

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
                                state = SchedulerState.Thinking;
                                break;
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

                            // It's a status update, so record elevator information
                            statuses[response.getElevator()].setFloor(response.getOriginFloor());
                            statuses[response.getElevator()].setDirection(response.getDirection());

                            // Set state back to idle
                            state = SchedulerState.Idle;
                            break;
                    }
            }
        }
    }

    /**
     * Choose the best possible elevator to send the request to.
     * 
     * @param request The request to be routed.
     * @return The ID of the elevator to route the request to.
     */
    public static int selectElevator(ElevatorRequest request) {

        // Schedule based on pick-up location only
        originFloor = request.getOriginFloor();

        int chosenElevator = 0; // The elevator selected for this request (by default pick the first one)
        int minDiff = 10000; // Initialize with the maximum possible difference.
        for (int i = 0; i < statuses.length; i++) {

            // Consider only elevators moving in the correct direction that haven't been
            // shut down
            if (!statuses[i].isShutDown() && request.getDirection() == statuses[i].getDirection()) {

                // Calculate the floor difference to find the closest elevator.
                int diff = abs(statuses[i].getFloor() - originFloor);
                if (diff < minDiff) {
                    chosenElevator = i; // Update the chosen elevator to the current closest one.
                    minDiff = diff; // Update the minimum difference found.
                }
            }
        }
        return chosenElevator;
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
