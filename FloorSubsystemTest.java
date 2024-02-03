import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FloorSubsystemTest {

    @Test
    void run() throws InterruptedException{
        // Initialize objects needed for the FloorSubsytem
        MessageQueue<ElevatorRequest> floorIncoming = new MessageQueue<>();
        MessageQueue<ElevatorRequest> floorOutgoing = new MessageQueue<>();
        FloorSubsystem test_fsys;
        try {
            test_fsys = new FloorSubsystem("./testdata.txt", floorIncoming, floorOutgoing);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file.");
            return;
        }

        // Start the floor subsystem to process the message
        Thread floorThread = new Thread(test_fsys);
        floorThread.start();
        floorIncoming.putMessage(new ElevatorRequest("14:05:15.0 2 Up 4"));
        Thread.sleep(300);

        // Check that FloorSubsystem put the request in the outgoing queue
        assertFalse(floorOutgoing.isEmpty());

        assertEquals(new ElevatorRequest("14:05:15.0 2 Up 4"), floorOutgoing.getMessage());

        // Check that the outgoing queue is empty once the FloorSubsystem exits
        assertNull(floorOutgoing.getMessage());

        // Stop the FloorSubsystem
        floorIncoming.putMessage(null);
        floorThread.join();
    }
}