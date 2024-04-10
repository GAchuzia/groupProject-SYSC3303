import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */

public class ElevatorSubsystemTest {

    /**
     * Checks if the message being sent to an elevator is sent to the correct port.
     */
    @Test
    public void testRouteToElevator() throws IOException {
        DatagramSocket mockSocket = new DatagramSocket();
        ElevatorRequest request = new ElevatorRequest(1, 1, 5, 2, 0, 5, 1, false);
        DatagramPacket message = new DatagramPacket(request.getBytes(), request.getBytes().length, InetAddress.getLocalHost(), ElevatorSubsystem.ELEVATOR_PORT_START + request.getElevator());

        ElevatorSubsystem.routeToElevator(message, mockSocket);

        assertEquals(ElevatorSubsystem.ELEVATOR_PORT_START + request.getElevator(), message.getPort());
    }

    /**
     *  Checks that the message being sent to the scheduler is being sent to the correct port.
     * @throws IOException
     */
    @Test
    public void testSendToScheduler() throws IOException {
        // Create a test socket
        DatagramSocket testSocket = new DatagramSocket();

        // Create a message with the destination address and port
        InetAddress address = InetAddress.getByName("localhost");
        int port = Scheduler.PORT;
        byte[] data = new byte[10];
        DatagramPacket message = new DatagramPacket(data, data.length, address, port);

        // Send message
        ElevatorSubsystem.sendToScheduler(message, testSocket);
        assertEquals(port, message.getPort());
    }

    /**
     * Checks that the correct number of initialized elevators are created with
     * the correct number of ports.
     * @throws SocketException
     */
    @Test
    public void testInitElevators() throws SocketException {
        Elevator[] elevators = ElevatorSubsystem.initElevators();

        // Check that the correct number of elevators are initialized
        assertEquals(ElevatorSubsystem.NUM_ELEVATORS, elevators.length);
        for (int i = 0; i < ElevatorSubsystem.NUM_ELEVATORS; i++) {
            // Checks that the correct number of ports are made for the elevators
            assertEquals(ElevatorSubsystem.ELEVATOR_PORT_START + i, elevators[i].getPort());
        }
    }

    /**
     * Initializes elevator threads and checks that the correct number of threads
     * are started. Also checks that the elevators are in the RUNNABLE state.
     * @throws InterruptedException
     * @throws SocketException
     */
    @Test
    public void testStartElevators() throws InterruptedException, SocketException {
        // Initialize an array of elevators
        Elevator[] elevators = new Elevator[3];
        for (int i = 0; i < elevators.length; i++) {
            elevators[i] = new Elevator(2012 + i);
        }

        // Make sure correct number of threads are created

        Thread[] threads = ElevatorSubsystem.startElevators(elevators);
        // Check that we started the correct number of threads for the elevators
        assertEquals(elevators.length, threads.length);
        for (Thread thread : threads) {
            // Check that the elevators are in the RUNNABLE state
            assertEquals(Thread.State.RUNNABLE, thread.getState());
        }
    }


}