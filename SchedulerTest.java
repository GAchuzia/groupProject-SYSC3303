import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {

    @Test
    void run() throws InterruptedException{
        //Initialize MessageQueues for the test
        MessageQueue<ElevatorRequest> floorIncoming = new MessageQueue<>();
        MessageQueue<ElevatorRequest> floorOutgoing = new MessageQueue<>();
        MessageQueue<ElevatorRequest> elevatorIncoming = new MessageQueue<>();
        MessageQueue<ElevatorRequest> elevatorOutgoing = new MessageQueue<>();

        // Make and start a scheduler thread
        Scheduler scheduler = new Scheduler(floorIncoming, floorOutgoing, elevatorIncoming, elevatorOutgoing);
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();

        // Send a message to the elevator from the scheduler
        ElevatorRequest floorMessage = new ElevatorRequest("14:05:15.0 2 Up 4");
        floorIncoming.putMessage(floorMessage);
        Thread.sleep(300);

        // Check that scheduler sent message to the elevator
        assertFalse(elevatorOutgoing.isEmpty());
        assertEquals(floorMessage, elevatorOutgoing.getMessage());

        // Send a message from the elevator to the scheduler
        ElevatorRequest elevatorMessage = new ElevatorRequest("14:10:00.0 4 Down 1");
        elevatorIncoming.putMessage(elevatorMessage);
        Thread.sleep(300);

        // Check that the scheduler forwarded the message to the floor
        assertFalse(floorOutgoing.isEmpty());
        assertEquals(elevatorMessage, floorOutgoing.getMessage());

        // Stop the scheduler
        elevatorIncoming.putMessage(null);
        Thread.sleep(300);
        schedulerThread.join();
    }
}