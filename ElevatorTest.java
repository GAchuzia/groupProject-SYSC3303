import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
class ElevatorTest {

    /** The message queue for the elevator under test to receive messages on. */
    private MessageQueue<ElevatorRequest> requestQueue;

    /**
     * The message queue for the elevator under test to send completion messages on.
     */
    private MessageQueue<ElevatorRequest> completionQueue;

    /** The elevator under test. */
    private Elevator elevator;

    /**
     * Sets up the elevator and its message queues before each test.
     */
    @BeforeEach
    void setUp() {
        requestQueue = new MessageQueue<>();
        completionQueue = new MessageQueue<>();
        elevator = new Elevator(requestQueue, completionQueue);
    }

    /**
     * Tears down the elevator and its message queues after each test.
     */
    @AfterEach
    void tearDown() {
        requestQueue = null;
        completionQueue = null;
        elevator = null;
    }

    /**
     * Tests that the elevator is initialized correctly using the default
     * constructor.
     */
    @Test
    void testDefaultConstructor() {
        Elevator e = new Elevator(requestQueue, completionQueue);
        assertEquals(1, e.getFloor());
    }

    /**
     * Test that the elevator moves between floors in accordance with its received
     * request.
     */
    @Test
    void testElevatorMovement() {
        ElevatorRequest elevator_request = new ElevatorRequest("14:15:45.0 5 Down 3");

        requestQueue.putMessage(elevator_request);

        Thread elevator_thread = new Thread(elevator);
        elevator_thread.start();

        try {
            Thread.sleep(1000); // Give the subsystem time to process the message
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(3, elevator.getFloor());
    }
}
