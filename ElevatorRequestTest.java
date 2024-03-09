import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains the test suite for the ElevatorRequest class.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
class ElevatorRequestTest {

    /**
     * A sample elevator request to perform testing on.
     */
    static final ElevatorRequest REQUEST = new ElevatorRequest("14:05:15.2 2 Up 4");

    /**
     * Check that the fields of the elevator request can be correctly filled from a parsed input string.
     */
    @Test
    void testParsing() {
        assertEquals(REQUEST.getDirection(), Direction.Up);
        assertEquals(REQUEST.getOriginFloor(), 2);
        assertEquals(REQUEST.getDestinationFloor(), 4);
        assertEquals(REQUEST.getTimestamp(), LocalTime.of(14, 5, 15, 200000000));
    }

    /**
     * Check that the string representation of the elevator request is generated correctly.
     */
    @Test
    void testToString() {
        assertEquals("Timestamp: 14:05:15.200 Direction: Up To: 4 From: 2", REQUEST.toString());
    }

    /**
     * Check that passing a bad input string to the ElevatorRequest constructor results in an error.
     */
    @Test
    void testBadConstruction() {
        assertThrows(DateTimeParseException.class, () -> new ElevatorRequest("this is a bad string"));
    }

    /**
     * Check that ElevatorRequest can be constructed from a byte array
     */
    @Test
    void testByteConstruction(){
        ElevatorRequest byteRequest = null;
        try {
            byteRequest = new ElevatorRequest(REQUEST.getBytes());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        assertEquals(REQUEST, byteRequest);
    }

    /**
     * Check that byte array request can be obtained
     */
    @Test
    void testGetByteArray(){
        byte[] bytes = REQUEST.getBytes();
        assertNotNull(bytes);
        assertEquals(100, bytes.length);
    }
}