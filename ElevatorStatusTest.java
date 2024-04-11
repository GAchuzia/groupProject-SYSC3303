import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

class ElevatorStatusTest {
    private ElevatorStatus elevatorStatus;

    @BeforeEach
    void setUp() {
        elevatorStatus = new ElevatorStatus();
    }

    /**
     * Test toString method returns a string representation of
     * the elevator status
     */
    @Test
    void testToString(){
        elevatorStatus.setElevator(2);
        elevatorStatus.setFloor(9);
        elevatorStatus.setDirection(Direction.Down);
        elevatorStatus.markShutDown();
        elevatorStatus.setRiders(9);

        String expected_str = "Status(id=2, floor=9, direction=Down, shut_down=true, riders=9)";
        assertEquals(expected_str, elevatorStatus.toString());
    }

    /**
     * Test getBytes method turns elevator status into bytes
     */
    @Test
    void testGetBytes(){
        elevatorStatus.setElevator(3);
        elevatorStatus.setFloor(6);
        elevatorStatus.setDirection(Direction.Up);
        elevatorStatus.markShutDown();
        elevatorStatus.setRiders(3);

        byte[] expectedBytes;
        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.putInt(6);
        buffer.putInt(Direction.Up.ordinal());
        buffer.putInt(1);
        buffer.putInt(3);
        buffer.putInt(3);
        expectedBytes = buffer.array();

        assertArrayEquals(expectedBytes, elevatorStatus.getBytes());
    }
}
