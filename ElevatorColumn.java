import javax.swing.*;
import java.awt.*;

/**
 * Represents a visual panel component for an elevator column in a GUI
 * application.
 *
 * It displays the elevator's current status, including its ID, floor,
 * direction, door status, and the number of riders. Additionally, it provides
 * visual indicators for faults and shutdown states.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class ElevatorColumn extends JPanel {

    /** Displays current floor. */
    private JLabel floorLabel;

    /* Visual representation of elevator movement. */
    private ElevatorSlider slider;

    /* Displays current number of riders. */
    private JLabel riderCounter;

    /* Displays the elevator ID */
    private JLabel idLabel;

    /* Displays current status, such as a fault message. */
    private JLabel statusLabel;

    /* Displays doors status (open or closed). */
    private JLabel doorsLabel;

    /** Icon with up arrow to show upwards movement. */
    private static final ImageIcon UP_ICON = new ImageIcon("Icons/upArrowIcon.png");

    /** Icon with down arrow to show downward movement. */
    private static final ImageIcon DOWN_ICON = new ImageIcon("Icons/downArrowIcon.png");

    /** Icon with "x" to show a fault that cannot be recovered from. */
    private static final ImageIcon HARD_FAULT = new ImageIcon("Icons/hardFaultIcon.png");

    /** Icon with "!" to show a fault that can be recovered from. */
    private static final ImageIcon FAULT = new ImageIcon("Icons/transientFaultIcon.png");

    /** The height for text labels. */
    private static final int LABEL_HEIGHT = 40;

    /** The font for text labels. */
    private static final Font LABEL_FONT = new Font("Sans Serif", Font.PLAIN, 15);

    /** The dimensions that will be used for text labels. */
    private Dimension labelDimensions;

    /** A string representation of opened elevator doors */
    private final static String DOORS_OPENS = "[|   |]";

    /** A string representation of closed elevator doors */
    private final static String DOORS_CLOSED = "[|]";

    /**
     * Constructs an ElevatorColumn panel.
     *
     * @param id     The identifier for the elevator.
     * @param width  The preferred width for the elevator panel.
     * @param height The preferred height for the elevator panel.
     */
    public ElevatorColumn(int id, int width, int height) {
        // Initialize the panel itself
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLACK);

        // Dimensions for all labels
        this.labelDimensions = new Dimension(width - 10, LABEL_HEIGHT);

        // Initialize slider to represent elevator movement
        this.slider = new ElevatorSlider(width, height - LABEL_HEIGHT * 5);

        // Initialize and set elevator ID label
        this.idLabel = this.newLabel("");
        this.idLabel.setText("Elevator #" + id);

        // Initialize floor label with UP_ICON by default
        this.floorLabel = this.newLabel("");
        this.floorLabel.setIcon(UP_ICON);
        this.goToFloor(FloorSubsystem.GROUND_FLOOR); // Start on ground floor

        // Initialize rider counter with 0 riders
        this.riderCounter = this.newLabel("");
        this.updateRiderCount(0);

        // Initialize doors label
        this.doorsLabel = newLabel(DOORS_CLOSED);
        this.doorsLabel.setHorizontalAlignment(JLabel.CENTER);

        // Add labels, door and slider to panel
        this.add(this.idLabel);
        this.add(this.floorLabel);
        this.add(this.slider);
        this.add(this.riderCounter);
        this.add(doorsLabel);

        // Initialize and add status label
        this.statusLabel = newLabel("<html><div style='text-align: center;'>Waiting for request</div></html>");
        this.statusLabel.setVerticalAlignment(JLabel.CENTER);
        this.statusLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(this.statusLabel);
    }

    /**
     * Creates and returns a new JLabel with specified text, centered alignment, and
     * predefined styles.
     *
     * @param text The text to display on the label.
     * @return A newly created JLabel with specified properties.
     */
    private JLabel newLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.white);
        label.setPreferredSize(this.labelDimensions);
        label.setSize(this.labelDimensions);
        return label;
    }

    /**
     * Moves the elevator to a specified floor.
     *
     * @param floor The floor number to move the elevator to.
     */
    public void goToFloor(int floor) {
        this.slider.setValue(floor);
        this.floorLabel.setText(String.valueOf(floor));
    }

    /**
     * Updates the display of the rider count in the elevator.
     *
     * @param riders The number of riders to display.
     */
    public void updateRiderCount(int riders) {
        this.riderCounter.setText("Riders: " + riders);
    }

    /**
     * Sets the direction of the elevator movement.
     *
     * @param direction The direction of the elevator (Up or Down).
     */
    public void setDirection(Direction direction) {
        this.floorLabel.setIcon(direction == Direction.Up ? UP_ICON : DOWN_ICON);
    }

    /**
     * Sets the doors' open or closed status.
     *
     * @param door true to set the doors as open, false to set as closed.
     */
    public void setDoor(boolean door) {
        this.doorsLabel.setText(door ? DOORS_OPENS : DOORS_CLOSED);
        updateStatus(door ? "Open Doors" : "Close Doors");
    }

    /**
     * Handles the elevator fault status updates.
     *
     * @param status   The fault status code.
     * @param floorNum The floor number where the fault occurred.
     */
    public void handleFault(int status, int floorNum) {
        if (status == -1) {
            updateStatus("Door is stuck closed. Trying again...");
        } else {
            updateStatus("Door is stuck opened. Trying again...");
        }
        this.floorLabel.setIcon(FAULT);
    }

    /**
     * Shuts down the elevator, indicating a severe fault.
     */
    public void shutDown() {
        for (int i = FloorSubsystem.GROUND_FLOOR; i <= FloorSubsystem.NUM_FLOORS; i++) {
            ((JLabel) slider.getLabelTable().get(i)).setText(String.valueOf(i));
            ((JLabel) slider.getLabelTable().get(i)).setForeground(Color.RED);
        }
        updateStatus("SHUT DOWN!");
        this.floorLabel.setIcon(HARD_FAULT);
    }

    /**
     * Updates the status label with a specified message.
     *
     * @param text The message to display as the status.
     */
    private void updateStatus(String text) {
        statusLabel.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
    }

    /**
     * Highlights a destination floor on the slider.
     *
     * @param floorNum The floor number to highlight.
     */
    public void highlightDestination(int floorNum) {
        ((JLabel) slider.getLabelTable().get(floorNum)).setForeground(Color.yellow);
        this.slider.repaint();
    }

    /**
     * Removes the highlight from a previously highlighted destination floor.
     *
     * @param floorNum The floor number to unhighlight.
     */
    public void unHighlightDestination(int floorNum) {
        ((JLabel) slider.getLabelTable().get(floorNum)).setForeground(Color.white);
        slider.repaint();
    }

    /**
     * Adds a star next to a floor number to indicate it is a destination floor.
     *
     * @param floorNum    The floor number where the star should be added.
     * @param destination The destination floor number.
     */
    public void addStar(int floorNum, int destination) {
        ((JLabel) slider.getLabelTable().get(floorNum)).setText(floorNum + " *");
        updateStatus("Request received to go to floor " + destination + " from floor " + floorNum + ".");
        this.slider.repaint();
    }

    /**
     * Removes the star from a floor number, indicating it is no longer a
     * destination floor.
     *
     * @param floorNum The floor number from which the star should be removed.
     */
    public void removeStar(int floorNum) {
        ((JLabel) slider.getLabelTable().get(floorNum)).setText(String.valueOf(floorNum));
        slider.repaint();
    }
}
