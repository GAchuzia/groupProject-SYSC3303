//import org.junit.jupiter.api.Test;
//
//import java.net.SocketException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Test suite for testing the functionality of the Elevator class.
// *
// * @author Matteo Golin, 101220709
// * @author Grant Achuzia, 101222695
// * @author Saja Fawagreh, 101217326
// * @author Javeria Sohail, 101197163
// * @author Yousef Hammad, 101217858
// * @version 0.0.0
// */
//class ElevatorTest {
//
//
//    /**
//     * Tests that the elevator is initialized correctly using the default
//     * constructor.
//     */
//    @Test
//    void testDefaultConstructor() throws SocketException {
//        Elevator elevator = new Elevator(1234);
//        assertEquals(1, elevator.getFloor());
//    }
//
//    /**
//     * Test that the elevator moves between floors in accordance with its received
//     * request.
//     */
//    @Test
//    void testElevatorMovement() throws SocketException {
//        Elevator elevator = new Elevator(4321);
//        elevator.moveElevatorTo(5);
//        assertEquals(5, elevator.getFloor());
//    }
//}
