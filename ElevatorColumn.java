import javax.swing.*;
import java.awt.*;

public class ElevatorColumn extends JPanel {

    private JLabel floorLabel;
    private ElevatorSlider slider;
    private JLabel riderCounter;
    private JLabel idLabel;

    private static final ImageIcon UP_ICON = new ImageIcon("Icons/upArrowIcon.png");
    private static final ImageIcon DOWN_ICON = new ImageIcon("Icons/downArrowIcon.png");

    private static final int LABEL_HEIGHT = 40;
    private static final Font LABEL_FONT = new Font("Sans Serif", Font.PLAIN, 20);
    private Dimension labelDimensions;

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
        this.idLabel = this.newLabel();
        this.idLabel.setText("Elevator #" + id);

        // Displays current floor and direction
        this.floorLabel = this.newLabel();
        this.floorLabel.setIcon(UP_ICON);
        this.goToFloor(FloorSubsystem.GROUND_FLOOR); // Start on ground floor (1)

        // Displays how many riders are in the elevator
        this.riderCounter = this.newLabel();
        this.updateRiderCount(0); // No riders at start time

        // Add in components
        this.add(this.idLabel);
        this.add(this.floorLabel);
        this.add(this.slider);
        this.add(this.riderCounter);
    }

    private JLabel newLabel() {
        JLabel label = new JLabel("", JLabel.CENTER);
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

    public void shutDown() {
        this.floorLabel.setText("SHUT DOWN!");
    }

}
