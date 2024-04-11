import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.net.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Iteration 3 Update: Since the FloorSubsystem class has no constructor
 * and doesn't use message queues anymore, the previous test cases are
 * deprecated.
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

    /** The testdata file. */
    public static final String TEST_FILE = "./testdata.txt";

    /** The string in the first line of the testdata.txt file. */
    public static final String FIRST_LINE_OF_FILE = "14:05:15.0 2 Up 4";

    /**
     * Tests that the first line of the test file is correctly parsed into an
     * elevator request by the floor subsystem.
     */
    @Test
    public void testNextRequestFirstLine() throws FileNotFoundException {
        File file = new File(TEST_FILE);
        Scanner reader = new Scanner(file);

        ElevatorRequest readRequest = FloorSubsystem.nextRequest(reader);
        ElevatorRequest correctRequest = new ElevatorRequest(FIRST_LINE_OF_FILE);

        assertEquals(correctRequest, readRequest);
    }

    /**
     * Tests that the nextRequest method of the FloorSubsystem returns null when
     * there are no more lines in the file.
     */
    @Test
    public void testNextRequestIsNull() throws FileNotFoundException {
        File file = new File(TEST_FILE);
        Scanner reader = new Scanner(file);

        // Read all of the lines in the file
        while (reader.hasNext()) {
            reader.nextLine();
        }

        ElevatorRequest readRequest = FloorSubsystem.nextRequest(reader);
        assertNull(readRequest);
    }

    /**
     * Tests that messages sent with the sendRequest method are 'received' by a mock
     * scheduler.
     *
     * WARNING: This test must be run when the port specified by `Scheduler.PORT` is
     * not in use.
     */
    @Test
    public void testSendRequest() throws IOException, SocketException {
        ElevatorRequest request = new ElevatorRequest(FIRST_LINE_OF_FILE);

        // Socket for the floor subsystem to use to send
        DatagramSocket socket = new DatagramSocket();

        // Socket that is the mock Scheduler
        DatagramSocket scheduler = new DatagramSocket(Scheduler.PORT);

        // Send the request
        FloorSubsystem.sendRequest(request, socket);

        // Receive the request
        DatagramPacket p = new DatagramPacket(new byte[100], 100);
        scheduler.receive(p);

        // Parse the received request to compare
        ElevatorRequest receivedRequest = new ElevatorRequest(p.getData());
        assertEquals(request, receivedRequest);
    }
}
