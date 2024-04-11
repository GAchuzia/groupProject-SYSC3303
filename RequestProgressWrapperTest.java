import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RequestProgressWrapperTest {
    /**
     *  Test that the wrapper tracks the elevator's progress correctly as it
     *  picks up and drops off a request.
     */
    @Test
    void testUpdateProgress() {
        // Elevator request for the 4th floor going to the 6th floor
        ElevatorRequest request = new ElevatorRequest(1, 4, 7, 2, 0, 7, 1, false);
        RequestProgressWrapper wrapper = new RequestProgressWrapper(request);

        // Request should not be picked up or dropped off yet
        assertFalse(wrapper.wasPickedUp());
        assertFalse(wrapper.wasDroppedOff());
        assertFalse(wrapper.isComplete());

        // Elevator picks up the request from the 4th floor
        wrapper.updateProgress(4);
        assertTrue(wrapper.wasPickedUp());
        assertFalse(wrapper.wasDroppedOff());
        assertFalse(wrapper.isComplete());

        // Elevator drops off request on the 7th floor
        wrapper.updateProgress(7);
        assertTrue(wrapper.wasPickedUp());
        assertTrue(wrapper.wasDroppedOff());
        assertTrue(wrapper.isComplete());
    }

    /**
     *  Test that the wrapper returns the correct elevator request
     */
    @Test
    void testGetRequest() {
        // Create an elevator request
        ElevatorRequest request = new ElevatorRequest(1, 3, 6, 2, 0, 6, 1, false);
        RequestProgressWrapper wrapper = new RequestProgressWrapper(request);

        // Make sure wrapped request is the same as original request
        ElevatorRequest wrappedRequest = wrapper.getRequest();
        assertEquals(request, wrappedRequest);
    }
}