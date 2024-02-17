import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite to cover the functionality of the ElevatorSubsystem.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
class ElevatorSubsystemTest {

    /**
     * The incoming message queue for the elevator subsystem.
     */
    private MessageQueue<ElevatorRequest> incoming;
    /**
     * The outgoing message queue for the elevator subsystem.
     */
    private MessageQueue<ElevatorRequest> outgoing;
    /**
     * The elevator subsystem itself.
     */
    private ElevatorSubsystem subsystem;

    /**
     * Initializes the ElevatorSubsystem with new message queues.
     */
    @BeforeEach
    protected void setUp() {
        this.incoming = new MessageQueue<>();
        this.outgoing = new MessageQueue<>();
        this.subsystem = new ElevatorSubsystem(incoming, outgoing, 1);
    }

    /**
     * Removes the subsystem and its message queues so they can be freshly replaced.
     */
    @AfterEach
    void tearDown() {
        this.subsystem = null;
        this.outgoing = null;
        this.incoming = null;
    }

    /**
     * Tests the echo behaviour of the ElevatorSubsystem when it receives a message.
     *
     * @throws InterruptedException
     */
    @Test
    void testEcho() throws InterruptedException {

        ElevatorRequest elevatorRequest = new ElevatorRequest("23:10:12.1 3 Down 2");

        // Start the elevator subsystem to process the message
        Thread elevatorThread = new Thread(this.subsystem);
        elevatorThread.start();
        assertTrue(elevatorThread.isAlive());

        // Put the message on the incoming queue
        incoming.putMessage(elevatorRequest);
        Thread.sleep(300); // Gives the ElevatorSubsytem thread time to process the message

        // Check that the ElevatorSubsystem put the request in the outgoing queue
        assertFalse(this.outgoing.isEmpty());
        assertEquals(elevatorRequest, this.outgoing.getMessage());

        // Stop the ElevatorSubsystem
        this.incoming.putMessage(null);
        elevatorThread.join();
    }

    /**
     * Test that the ElevatorSubsystem exits when it receives a null message and passes the null message back.
     */
    @Test
    void testExit() throws InterruptedException {

        // Start subsystem thread
        Thread elevatorThread = new Thread(this.subsystem);
        elevatorThread.start();

        this.incoming.putMessage(null);
        Thread.sleep(300); // Give the subsystem time to process the message
        assertFalse(elevatorThread.isAlive());
    }
}