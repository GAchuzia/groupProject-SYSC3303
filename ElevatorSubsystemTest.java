import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorSubsystemTest {

    @Test
    void run() throws InterruptedException {
        // Initialize objects needed for the elevator sim
        MessageQueue<ElevatorRequest> incoming = new MessageQueue<>();
        MessageQueue<ElevatorRequest> outgoing = new MessageQueue<>();
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(incoming, outgoing);
        ElevatorRequest elevatorRequest = new ElevatorRequest("23:10:12.1 3 Down 2");
        Thread elevatorThread = new Thread(elevatorSubsystem);

        // Start the elevator susystem to process the message
        elevatorThread.start();
        incoming.putMessage(elevatorRequest);
        Thread.sleep(300); // Gives the ElevatorSubsytem time to process the message

        // Check that the ElevatorSubsystem put the request in the outgoing queue
        assertFalse(outgoing.isEmpty());
        assertEquals(elevatorRequest, outgoing.getMessage());

        // Check that the outgoing queue is empty once the ElevatorSubsystem exits
        assertTrue(outgoing.isEmpty());


    }
}