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


    @Test
    public void testMoveTo() throws SocketException{
        Elevator elevator = new Elevator(2003);
        assertEquals(1, elevator.getFloor()); // Check that we start on first floor

        int destinationFloor =4;
        int randomNumber = elevator.nextRandomNum();
        boolean result = elevator.moveTo(destinationFloor, randomNumber);

        if (randomNumber <= 5) {
            assertEquals(ElevatorState.Halted, elevator.getState());
        }  else {
            // If randomNumber > 5, elevator should have moved to destinationFloor
            assertEquals(destinationFloor, elevator.getFloor());
            assertEquals(ElevatorState.Idle, elevator.getState());
        }
  }

    @Test
    public void testOpenDoors() throws SocketException {
        Elevator elevator = new Elevator(2004);
        ByteArrayOutputStream streamOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(streamOutput));

        int randomNumber = 31; // Set randomNumber to a value greater than 30 to ensure doors open
        elevator.openDoors(randomNumber);

        // Check that proper open door message prints
        assertTrue(streamOutput.toString().contains("Elevator #" + elevator.getId() + " door opened"));
    }

    @Test
    public void testCloseDoors() throws SocketException {
        Elevator elevator = new Elevator(2005);
        ByteArrayOutputStream streamOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(streamOutput));

        int randomNumber = 31; // Set randomNumber to a value greater than 30 to ensure doors close
        elevator.closeDoors(randomNumber);

        // Check that proper closed door message prints
        assertTrue(streamOutput.toString().contains("Elevator #" + elevator.getId() + " door closed"));
    }

    @Test
    public void testNextFloorUp() throws SocketException {
        Elevator elevator = new Elevator(2006);
        elevator.floor_q.add(10);
        elevator.floor_q.add(15);

        elevator.floor = 11;
        elevator.direction = Direction.Up;

        assertEquals(Integer.valueOf(15), elevator.nextFloor());
    }

    @Test
    public void testToggleDirection() throws SocketException {
        Elevator elevator = new Elevator(2007);

        // Initial direction to Up
        elevator.direction = Direction.Up;
        elevator.toggleDirection();
        assertEquals(Direction.Down, elevator.direction);

        // Initial direction to Down
        elevator.direction = Direction.Down;
        elevator.toggleDirection();
        assertEquals(Direction.Up, elevator.direction);
    }
}