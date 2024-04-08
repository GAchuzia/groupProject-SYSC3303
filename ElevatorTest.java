import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;

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

    /**
     * Tests that a random number greater than five does not trigger a fault, and
     * the elevator successfully moves floors.
     */
//    @Test
//    public void testMoveTo() throws SocketException {
//        Elevator elevator = new Elevator(2003);
//        int destinationFloor = 4;
//        boolean result = elevator.moveTo(destinationFloor, 10);
//
//        assertTrue(result);
//        assertEquals(destinationFloor, elevator.getFloor());
//        assertEquals(ElevatorState.Idle, elevator.getState());
//    }

    /**
     * Test that a random number greater than 30 results in the doors successfully
     * opening.
     */
    @Test
    public void testOpenDoors() throws SocketException {
        Elevator elevator = new Elevator(2004);
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
        Elevator elevator = new Elevator(2005);
        ByteArrayOutputStream streamOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(streamOutput));

        elevator.closeDoors(31);

        // Check that proper closed door message prints
        assertTrue(streamOutput.toString().contains("Elevator #" + elevator.getId() + " door closed"));
    }

    /**
     * Tests that requesting the next floor with no floors to process returns null.
     */
//    @Test
//    public void testNextFloorUp() throws SocketException {
//        Elevator elevator = new Elevator(2006);
//        assertEquals(null, elevator.nextFloor());
//    }

    /**
     * Tests that toggling direction results in the opposite direction of the
     * elevator.
     */
    @Test
    public void testToggleDirection() throws SocketException {
        Elevator elevator = new Elevator(2007);

        Direction initial_direction = elevator.getDirection();
        elevator.toggleDirection();
        assertNotEquals(initial_direction, elevator.getDirection());
    }
}
