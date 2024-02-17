import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {

    private MessageQueue<ElevatorRequest> requestQueue;
    private MessageQueue<ElevatorRequest> completionQueue;
    private Elevator elevator;

    @BeforeEach
    void setUp() {
        requestQueue = new MessageQueue<>();
        completionQueue = new MessageQueue<>();
        elevator = new Elevator(requestQueue, completionQueue);
    }

    @AfterEach
    void tearDown() {
        requestQueue = null;
        completionQueue = null;
        elevator = null;
    }

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
