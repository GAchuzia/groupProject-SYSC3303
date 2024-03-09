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

    /** The current elevator chosen by the Scheduler to take the request (starts with elevator 0) */
    static int chosenElevator = -1;

    static int originFloor;

    static int elevatorNum;
    static ArrayList<Integer> list = new ArrayList<>(4);

    /** Executes the main logical loop of the Scheduler subsystem. */
    public static void main(String[] args) throws SocketException, IOException {

        // Create socket for receiving and sending
        DatagramSocket channel = new DatagramSocket(PORT);

        // The message buffer for receiving new UDP messages
        DatagramPacket message = null;

        for (int i = 0; i < 4; i++) {
            list.add(0);
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

                            // TODO: handle IPs from different computers
                            channel.send(message);

                            // TODO: Schedule based on elevator availabilty
                            // For now just sending to elevator 0 every time
                            ElevatorRequest request = new ElevatorRequest(message.getData());
                            int minDiff = 8;
                            for (int i = 0; i < 4; i++) {
                                if (list.get(i) != -1 ){
                                    int diff = abs(list.get(i) - originFloor);
                                    if(diff < minDiff){
                                        chosenElevator = i;
                                        minDiff = diff;
                                    }
                                }
                            }
                            list.set(chosenElevator, -1);
                            request.setElevator(chosenElevator);
                            message.setData(request.getBytes()); // Re-encode message
                            message.setPort(ElevatorSubsystem.PORT);
                            channel.send(message);

                            System.out.println("Scheduler forwarded floor message.");
                            break;

                        // If there is a message from the elevator subsystem, forward it to the floor
                        case ElevatorSubsystem.PORT:
                            state = SchedulerState.Thinking;

                            // Check if the message is a status update or complete request
                            // TODO: record elevator whereabouts somewhere to inform scheduling decisions
                            ElevatorRequest response = new ElevatorRequest(message.getData());

                            // Complete messages are sent to floor
                            if (response.isComplete()) {
                                message.setPort(FloorSubsystem.PORT);
                                channel.send(message);
                                System.out.println("Scheduler forwarded elevator message.");
                                elevatorNum = response.getElevator();
                                list.set(elevatorNum, response.getDestinationFloor());
                            }

                            break;
                    }

                    // Set state back to idle
                    state = SchedulerState.Idle;
                    break;
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
