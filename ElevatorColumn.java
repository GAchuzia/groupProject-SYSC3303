import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ElevatorColumn extends JPanel {

    private JLabel floorLabel;
    private ElevatorSlider slider;
    private JLabel riderCounter;
    private JLabel idLabel;
    private JLabel statusLabel;
    private JLabel doorsLabel;

    private static final ImageIcon UP_ICON = new ImageIcon("Icons/upArrowIcon.png");
    private static final ImageIcon DOWN_ICON = new ImageIcon("Icons/downArrowIcon.png");
    private static final ImageIcon HARD_FAULT = new ImageIcon("Icons/hardFaultIcon.png");
    private static final ImageIcon FAULT = new ImageIcon("Icons/transientFaultIcon.png");
    private static final int LABEL_HEIGHT = 40;
    private static final Font LABEL_FONT = new Font("Sans Serif", Font.PLAIN, 15);
    private Dimension labelDimensions;
    private final static String DOORS_OPENS = "[|   |]";
    private final static String DOORS_CLOSED = "[|]";

    public ElevatorColumn(int id, int width, int height) {

        // Initialize the panel itself
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLACK);

        // Create components for within panel

        // Dimensions for all labels
        this.labelDimensions = new Dimension(width - 10, LABEL_HEIGHT);

        // Remove enough height to also fit three labels
        this.slider = new ElevatorSlider(width, height - LABEL_HEIGHT * 3);

        // Displays the elevator ID
        this.idLabel = this.newLabel("");
        this.idLabel.setText("Elevator #" + id);

        // Displays current floor and direction
        this.floorLabel = this.newLabel("");
        this.floorLabel.setIcon(UP_ICON);
        this.goToFloor(FloorSubsystem.GROUND_FLOOR); // Start on ground floor (1)

        // Displays how many riders are in the elevator
        this.riderCounter = this.newLabel("");
        this.updateRiderCount(0); // No riders at start time

        // Add in components
        this.add(this.idLabel);
        this.add(this.floorLabel);
        this.add(this.slider);

        // Adding doors label here so it appears above the rider counter
        this.doorsLabel = newLabel(DOORS_CLOSED);
        this.doorsLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(doorsLabel); // Add doors label to the panel

        this.add(this.riderCounter); // Now the doors label will be above this

        this.statusLabel = newLabel("<html><div style='text-align: center;'>Waiting for request</div></html>");
        this.statusLabel.setVerticalAlignment(JLabel.CENTER);
        this.statusLabel.setHorizontalAlignment(JLabel.CENTER);

        this.add(this.statusLabel);
    }

    private JLabel newLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.white);
        label.setPreferredSize(this.labelDimensions);
        label.setSize(this.labelDimensions);
        return label;
    }

    public void goToFloor(int floor) {
        this.slider.setValue(floor);
        this.floorLabel.setText(String.valueOf(floor));
    }

    public void updateRiderCount(int riders) {
        this.riderCounter.setText("Riders: " + riders);
    }

    public void setDirection(Direction direction) {
        this.floorLabel.setIcon(direction == Direction.Up ? UP_ICON : DOWN_ICON);
    }

    public void setDoor(boolean door) {
        this.doorsLabel.setText(door ? DOORS_OPENS : DOORS_CLOSED);
        String text = door ? "Open Doors" : "Close Doors";
        updateStatus(text);
    }

    public void handleFault(int status, int floorNum) {
        if (status == -1){
            updateStatus("Door is stuck closed. Trying again...");
        }
        else {
            updateStatus("Door is stuck opened. Trying again...");
        }
        this.floorLabel.setIcon(FAULT);
    }

    public void shutDown() {
        for (int i = FloorSubsystem.GROUND_FLOOR; i <= FloorSubsystem.NUM_FLOORS; i++) {
            ((JLabel) slider.getLabelTable().get(i)).setText(String.valueOf(i));
            ((JLabel) slider.getLabelTable().get(i)).setForeground(Color.RED);
        }
        updateStatus("SHUT DOWN!");
        this.floorLabel.setIcon(HARD_FAULT);
    }

    private void updateStatus(String text) {
        statusLabel.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
    }

    public void highlightDestination(int floorNum) {
        ((JLabel) slider.getLabelTable().get(floorNum)).setForeground(Color.yellow);
        this.slider.repaint();
    }

    public void unHighlightDestination(int floorNum) {
        ((JLabel) slider.getLabelTable().get(floorNum)).setForeground(Color.white);
        slider.repaint();
    }

    public void addStar(int floorNum, int destination) {
        ((JLabel) slider.getLabelTable().get(floorNum)).setText(floorNum + " *");
        updateStatus("Request received to go to floor " + destination + " from floor " + floorNum + ".");
        this.slider.repaint();
    }

    public void removeStar(int floorNum) {
        ((JLabel) slider.getLabelTable().get(floorNum)).setText(String.valueOf(floorNum));
        slider.repaint();
    }
}
