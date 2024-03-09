import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Iteration 3 Update: Since the Scheduler class has no constructor
 * and doesn't use message queues anymore, the previous test cases are deprecated.
 * Test suite to cover the functionality of the Scheduler.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
//
//class SchedulerTest {
//
//    /**
//     * The message queue for messages going into the FloorSubsystem.
//     */
//    private MessageQueue<ElevatorRequest> floorIncoming;
//    /**
//     * The message queue for messages coming out of the FloorSubsystem.
//     */
//    private MessageQueue<ElevatorRequest> floorOutgoing;
//    /**
//     * The message queue for messages going into the ElevatorSubsystem.
//     */
//    private MessageQueue<ElevatorRequest> elevatorIncoming;
//    /**
//     * The message queue for messages going out of the ElevatorSubsystem.
//     */
//    private MessageQueue<ElevatorRequest> elevatorOutgoing;
//
//    /**
//     * The scheduler subsystem itself.
//     */
//    private Scheduler scheduler;
//
//    /**
//     * Initialize scheduler with the message queues.
//     */
//    @BeforeEach
//    void setUp() {
//        this.floorIncoming = new MessageQueue<>();
//        this.floorOutgoing = new MessageQueue<>();
//        this.elevatorIncoming = new MessageQueue<>();
//        this.elevatorOutgoing = new MessageQueue<>();
//    }
//
//    /**
//     * Wipe all message queues and subsystems to be newly constructed before the next test.
//     */
//    @AfterEach
//    void tearDown() {
//        this.floorIncoming = null;
//        this.floorOutgoing = null;
//        this.elevatorIncoming = null;
//        this.elevatorOutgoing = null;
//        this.scheduler = null;
//    }
//
//    @Test
//    void run() throws InterruptedException {
//        // Make and start a scheduler thread
//        Thread schedulerThread = new Thread(String.valueOf(scheduler));
//        schedulerThread.start();
//
//        // Send a message to the elevator from the floor
//        ElevatorRequest floorMessage = new ElevatorRequest("14:05:15.0 2 Up 4");
//        floorOutgoing.putMessage(floorMessage);
//        Thread.sleep(300);
//
//        // Check that scheduler sent message to the elevator
//        assertFalse(elevatorIncoming.isEmpty());
//        assertEquals(floorMessage, elevatorIncoming.getMessage());
//
//        // Send a message from the elevator to the scheduler
//        ElevatorRequest elevatorMessage = new ElevatorRequest("14:10:00.0 4 Down 1");
//        elevatorOutgoing.putMessage(elevatorMessage);
//        Thread.sleep(300);
//
//        // Check that the scheduler forwarded the message to the floor
//        assertFalse(floorIncoming.isEmpty());
//        assertEquals(elevatorMessage, floorIncoming.getMessage());
//
//        // Stop the scheduler
//        elevatorOutgoing.putMessage(null);
//        Thread.sleep(300);
//        schedulerThread.join();
//    }
//}