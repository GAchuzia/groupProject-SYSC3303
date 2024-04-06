import javax.swing.*;
import java.awt.*;

public class ElevatorColumn extends JPanel {

    private JLabel floorLabel;
    private ElevatorSlider slider;
    private JLabel riderCounter;

    private static final ImageIcon UP_ICON = new ImageIcon("Icons/upArrowIcon.png");
    private static final ImageIcon DOWN_ICON = new ImageIcon("Icons/downArrowIcon.png");

    private static final int LABEL_WIDTH = 100;
    private static final int LABEL_HEIGHT = 25;

    public ElevatorColumn() {

        // Initialize the panel itself
        super(new GridLayout(4, 1));
        this.setBackground(Color.BLACK);

        // Create components for within panel
        this.slider = new ElevatorSlider();

        this.floorLabel = new JLabel("", JLabel.CENTER);
        this.floorLabel.setForeground(Color.white);
        this.floorLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        this.floorLabel.setIcon(UP_ICON);
        this.goToFloor(1); // Start on ground floor (1)

        this.riderCounter = new JLabel("", JLabel.CENTER);
        this.riderCounter.setForeground(Color.white);
        this.riderCounter.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        this.updateRiderCount(0); // No riders at start time

        // Add in components
        this.add(this.floorLabel);
        this.add(this.slider);
        this.add(this.riderCounter);
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

}
