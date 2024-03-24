import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.PrintStream;
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
    public void testSendLocationUpdate(){
        // In progress
    }

    @Test
    public void testMoveTo() throws IOException {
        // Create an Elevator instance
        Elevator elevator = new Elevator(1234);

        // Call the moveTo method with a destination floor
        boolean result = elevator.moveTo(5, 0);

        // Assert that the elevator moved successfully
        assertTrue(result);
    }

    @Test
    public void testOpenDoors() throws SocketException {
        // Create an Elevator instance
        Elevator elevator = new Elevator(1234);

        // Call the openDoors method
        elevator.openDoors(0); // Simulate no fault
    }

    @Test
    public void testCloseDoors() throws SocketException {
        // Create an Elevator instance
        Elevator elevator = new Elevator(1234);

        // Call the closeDoors method
        elevator.closeDoors(0); // Simulate no faults
    }

    @Test
    public void testNextFloor(){
        // In progress
    }

    @Test
    public void testToggleDirection(){
        // In progress
    }
    @Test
    public void testNextRandomNum() throws SocketException {
        // Create an Elevator instance
        Elevator elevator = new Elevator(1234);

        // Call the nextRandomNum method
        int randomNumber = elevator.nextRandomNum();

        // Assert that the random number is between 1 and 100
        assertTrue(randomNumber >= 1 && randomNumber <= 100);
    }

}