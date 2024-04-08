import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RequestProgressWrapperTest {
    /**
     *  Test that the wrapper tracks the elevator's progress correctly
     */
    @Test
    void testUpdateProgress() {
        // Create a request from floor 1 to floor 5 with 3 riders
        ElevatorRequest request = new ElevatorRequest(1, 1, 7, 4);
        RequestProgressWrapper wrapper = new RequestProgressWrapper(request);

        // Request should not be picked up yet
        assertFalse(wrapper.wasPickedUp());
        // Request shouldn't be dropped  off yet
        assertFalse(wrapper.wasDroppedOff());
        // Request should be incomplete
        assertFalse(wrapper.isComplete());

        // Update progress to floor 1 (pick-up)
        wrapper.updateProgress(1);
        // Request should be picked up once original floor is visited
        assertTrue(wrapper.wasPickedUp());
        // Request shouldn't be dropped  off yet
        assertFalse(wrapper.wasDroppedOff());
        // Request should still be incomplete
        assertFalse(wrapper.isComplete());

        // Update progress to floor 5 (drop-off)
        wrapper.updateProgress(7);
        // Request should be dropped pff after visitation floor has been visited
        assertTrue(wrapper.wasDroppedOff());
        // Request should now be complete
        assertTrue(wrapper.isComplete());
    }

    /**
     *  Test that the wrapper returns the ElevatorRequest
     */
    @Test
    void testGetRequest() {
        // Create a request
        ElevatorRequest request = new ElevatorRequest(2, 2, 4, 2);
        RequestProgressWrapper wrapper = new RequestProgressWrapper(request);

        // Check if the request is correct
        assertEquals(request, wrapper.getRequest());
    }
}