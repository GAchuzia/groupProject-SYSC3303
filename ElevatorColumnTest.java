import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorColumnTest {


    /**
     * Test that updateRiderCount method updates the rider counter label correctly.
     */
    @Test
    void testUpdateRiderCount() {
        int id = 1;
        int width = 200;
        int height = 400;
        ElevatorColumn elevatorColumn = new ElevatorColumn(id, width, height);

        int riders = 3;
        elevatorColumn.updateRiderCount(riders);

        // Get the rider counter label
        JLabel riderCounter = (JLabel) elevatorColumn.getComponent(3);

        assertEquals("Riders: " + riders, riderCounter.getText());
    }


}