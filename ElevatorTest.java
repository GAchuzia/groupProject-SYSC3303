import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.CountDownLatch;

/**
 * Test suite for testing the functionality of the Elevator class.
 * 
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class ElevatorTest {
    private Elevator elevator;

    /**
     * Test that elevator correctly moves to the next floor in the right direction.
     * Also checks that the elevator does not try to move below the ground floor.
     */
    @Test
    public void testMove() throws SocketException {
        elevator = new Elevator(1998);

        // Elevator moves up a floor
        elevator.move(6); // randomNumber > 5 to not trigger fault
        assertEquals(2, elevator.getFloor());

        // Elevator moves down a floor
        elevator.toggleDirection();
        elevator.move(6);
        assertEquals(1, elevator.getFloor());

        // Elevator tries to go below ground floor
        assertFalse(elevator.move(6));

        // Fault is triggered
        elevator.toggleDirection();
        assertFalse(elevator.move(1)); // randomNumber < 5 to trigger fault
    }

    /**
     * Test that a random number greater than 30 results in the doors successfully
     * opening.
     */
    @Test
    public void testOpenDoors() throws SocketException {
        elevator = new Elevator(2004);
        ByteArrayOutputStream streamOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(streamOutput));

        // Set randomNumber to a value greater than 30 to ensure doors open
        elevator.openDoors(31);

        // Check that proper open door message prints
        assertTrue(streamOutput.toString().contains("Elevator #" + elevator.getId() + " door opened"));
    }

    /**
     * Test that a random number greater than 30 results in the doors closing
     * successfully.
     */
    @Test
    public void testCloseDoors() throws SocketException {
        elevator = new Elevator(2005);
        ByteArrayOutputStream streamOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(streamOutput));

        elevator.closeDoors(31);

        // Check that proper closed door message prints
        assertTrue(streamOutput.toString().contains("Elevator #" + elevator.getId() + " door closed"));
    }

    /**
     * Tests that requesting the next floor with no floors to process returns null.
     */
    @Test
    public void testNextFloorUp() throws SocketException {
        elevator = new Elevator(2006);
        assertEquals(false, elevator.floorsInDirection());
    }

    /**
     * Tests that toggling direction results in the opposite direction of the
     * elevator.
     */
    @Test
    public void testToggleDirection() throws SocketException {
        elevator = new Elevator(2007);
        Direction initial_direction = elevator.getDirection();
        elevator.toggleDirection();
        assertNotEquals(initial_direction, elevator.getDirection());
    }

    /**
     * Tests if the elevator is at a floor that needs to be stopped at.
     */
    @Test
    public void testAtStop() throws SocketException {
        elevator = new Elevator(2008);

        elevator.move(90);
        assertFalse(elevator.atStop());
    }


    /**
     * Tests that the elevator sends a completion message to the ElevatorSubsystem
     * when it is finished a request.
     * WARNING: The port `ElevatorSubsystem.PORT` cannot be in use when this test is
     * run.
     */
    @Test
    public void testUpdateRequests() throws IOException, InterruptedException {
        int port = 2009;
        elevator = new Elevator(port);

        // Test request for the elevator to process
        ElevatorRequest request = new ElevatorRequest("14:05:15.0 2 Up 4");

        // Mock ElevatorSubsystem
        DatagramSocket socket = new DatagramSocket(ElevatorSubsystem.PORT);

        // Send the request to the elevator
        new Thread(elevator).start();
        byte[] data = request.getBytes();
        DatagramPacket p = new DatagramPacket(data, data.length);
        p.setPort(port);
        p.setAddress(InetAddress.getLocalHost());
        socket.send(p);


        // Wait to receive the response from the elevator that the request is complete
        DatagramPacket response = new DatagramPacket(new byte[100], 100);

        socket.receive(response);
        ElevatorRequest parsed_response = new ElevatorRequest(response.getData());

        while (!parsed_response.isComplete()) {
            socket.receive(response);
            parsed_response = new ElevatorRequest(response.getData());
        }
        assertTrue(parsed_response.isComplete());
        assertEquals(request.getTimestamp(), parsed_response.getTimestamp());
    }
}
