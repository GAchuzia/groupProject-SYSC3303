import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorRequestTest {

    @Test
    void testToString() {
        // Make a test ElevatorRequest and compare it to an expected ElevatorRequest using the toString method
        String testString = "14:05:15.2 2 Up 4";
        ElevatorRequest elevatorRequest = new ElevatorRequest(testString);
        String expectedResult = "Timestamp: 14:05:15.200 Direction: Up To: 4 From: 2";
        assertEquals(expectedResult, elevatorRequest.toString());
    }
}