import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the functionality of the FloorSubsystem.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
class FloorSubsystemTest {

    /**
     * Test input file containing elevator request data.
     */
    private static final String TEST_INPUT_FILE = "./testdata.txt";

    /**
     * Message queue for incoming messages to the floor subsystem.
     */
    private MessageQueue<ElevatorRequest> incoming;
    /**
     * Message queue for outgoing messages from the floor subsystem.
     */
    private MessageQueue<ElevatorRequest> outgoing;

    /**
     * The floor subsystem itself.
     */
    private FloorSubsystem subsystem;

    /**
     * Sets up the subsystem with its incoming and outgiong message queues.
     */
    @BeforeEach
    void setUp() {
        this.incoming = new MessageQueue<>();
        this.outgoing = new MessageQueue<>();
        try {
            this.subsystem = new FloorSubsystem(TEST_INPUT_FILE, this.incoming, this.outgoing);
        } catch (FileNotFoundException e) {
            fail("Could not open input file.");
        }
    }

    /**
     * Removes the subsystem and its message queues so that they can be freshly replaced.
     */
    @AfterEach
    void tearDown() {
        this.subsystem = null;
        this.outgoing = null;
        this.incoming = null;
    }

    /**
     * Tests that the FloorSubsystem can send elevator requests.
     *
     * @throws InterruptedException
     */
    @Test
    void testRunning() throws InterruptedException {
        // Start the floor subsystem to produce messages
        Thread floorThread = new Thread(this.subsystem);
        floorThread.start();
        Thread.sleep(1500); // Give the subsystem time to produce messages

        // Check that FloorSubsystem put a request in the outgoing queue which matches the first line of the input file
        assertFalse(this.outgoing.isEmpty());
        try {
            File file = new File(TEST_INPUT_FILE);
            Scanner reader = new Scanner(file);
            String line = reader.nextLine();
            assertEquals(new ElevatorRequest(line), this.outgoing.getMessage());
        } catch (FileNotFoundException e) {
            fail(e);
        }

        // Stop the FloorSubsystem
        this.incoming.putMessage(null);
        floorThread.join();
    }

    /**
     * Tests that the FloorSubsystem exits when it receives a null message.
     */
    @Test
    void testExit() throws InterruptedException {
        // Start thread
        Thread floorThread = new Thread(this.subsystem);
        floorThread.start();
        assertTrue(floorThread.isAlive());

        this.incoming.putMessage(null);
        Thread.sleep(300); // Allow subsystem to receive null message
        assertFalse(floorThread.isAlive());
    }

    /**
     * Tests that the floor subsystem will fail to construct when the input file does not exist.
     */
    @Test
    void testBadFile() {
        assertThrows(FileNotFoundException.class, () -> {
            new FloorSubsystem("thisfiledefinitelydoesnotexist.lol", this.incoming, this.outgoing);
        });
    }
}