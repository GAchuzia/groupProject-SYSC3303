
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

class SchedulerTest {
    /**
     * Check that elevator status records are updated based on elevator requests.
     */
    @Test
    public void testUpdateRecords() {
        ElevatorStatus[] statuses = new ElevatorStatus[3];
        for (int i = 0; i < statuses.length; i++) {
            statuses[i] = new ElevatorStatus();
        }

        ElevatorRequest response = new ElevatorRequest(1, 1, 5, 2, 0, 5, 1, false);
        Scheduler.updateRecords(statuses, response);

        assertEquals(1, statuses[1].getElevator());
        assertEquals(1, statuses[1].getFloor());
        assertEquals(Direction.Up, statuses[1].getDirection());
        assertEquals(2, statuses[1].getRiders());
        assertEquals(0, statuses[1].getDoor());
        assertEquals(5, statuses[1].getDestinationFloor());
        assertEquals(1, statuses[1].getOriginFloor());
        assertEquals(false, statuses[1].isComplete());
    }



    /**
     * Tests that selectElevator selects the closest elevator regardless of direction
     * for a request if there are no elevators going in the desired direction.
     */
//    @Test
//    public void testSelectElevator() {
//    }


}