import javax.swing.*;
import java.awt.*;

public class ElevatorColumn extends JPanel {

    private JLabel floorLabel;
    private ElevatorSlider slider;
    private JLabel riderCounter;

    private static final ImageIcon UP_ICON = new ImageIcon("Icons/upArrowIcon.png");
    private static final ImageIcon DOWN_ICON = new ImageIcon("Icons/downArrowIcon.png");

    private static final int LABEL_HEIGHT = 2;
    private static final Font LABEL_FONT = new Font("Sans Serif", Font.PLAIN, 20);
    private Dimension labelDimensions;

    public ElevatorColumn(int width, int height) {

        // Initialize the panel itself
        super(new GridLayout(4, 1));
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLACK);

        // Create components for within panel
        this.slider = new ElevatorSlider(width, height);

        this.labelDimensions = new Dimension(width, LABEL_HEIGHT);

        this.floorLabel = this.newLabel();
        this.floorLabel.setIcon(UP_ICON);
        this.goToFloor(FloorSubsystem.GROUND_FLOOR); // Start on ground floor (1)

        this.riderCounter = this.newLabel();
        this.riderCounter.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        this.updateRiderCount(0); // No riders at start time

        // Add in components
        this.add(this.floorLabel);
        this.add(this.slider);
        this.add(this.riderCounter);
    }

    private JLabel newLabel() {
        JLabel label = new JLabel("", JLabel.CENTER);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.white);
        label.setMaximumSize(this.labelDimensions);
        label.setPreferredSize(this.labelDimensions);
        label.setMinimumSize(this.labelDimensions);
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

}
