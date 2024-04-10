
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
    @Test
    public void testSelectElevator() {
        ElevatorStatus[] statuses = new ElevatorStatus[3];

        // At floor 6, going up
        statuses[0] = new ElevatorStatus();
        statuses[0].setElevator(0);
        statuses[0].setFloor(6);
        statuses[0].setDirection(Direction.Up);

        // At floor 3, going down
        statuses[1] = new ElevatorStatus();
        statuses[1].setElevator(1);
        statuses[1].setFloor(3);
        statuses[1].setDirection(Direction.Down);

        // At floor 5, going down
        statuses[2] = new ElevatorStatus();
        statuses[2].setElevator(2);
        statuses[2].setFloor(5);
        statuses[2].setDirection(Direction.Down);

        ElevatorRequest request = new ElevatorRequest(0, 1, 4, 1, 0, 4, 1, false); // Request from floor 1 to 4

        int selectedElevator = Scheduler.selectElevator(statuses, request);
        assertEquals(1, selectedElevator);
    }


}