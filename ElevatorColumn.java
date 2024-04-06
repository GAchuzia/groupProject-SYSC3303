import javax.swing.*;
import java.awt.*;

public class ElevatorColumn extends JPanel {

    private int elevatorID;

    private JLabel floorLabel;
    private ElevatorSlider slider;
    private JLabel doorsLabel;
    private JLabel statusLabel;

    private static final ImageIcon UP_ICON = new ImageIcon("Icons/upArrowIcon.png");
    private static final ImageIcon DOWN_ICON = new ImageIcon("Icons/downArrowIcon.png");

    public ElevatorColumn(int id) {

        // Initialize the panel itself
        super(new GridLayout(4, 1));
        this.setBackground(Color.BLACK);
        this.elevatorID = id; // To track which elevator this column represents

        // Create components for within panel
        this.slider = new ElevatorSlider();

        this.floorLabel = new JLabel("1", JLabel.CENTER);
        this.floorLabel.setForeground(Color.white);
        this.floorLabel.setPreferredSize(new Dimension(100, 25));
        this.floorLabel.setIcon(UP_ICON);

        this.doorsLabel = new JLabel("Doors open.", JLabel.CENTER);
        this.doorsLabel.setForeground(Color.white);
        this.doorsLabel.setPreferredSize(new Dimension(100, 25));

        this.statusLabel = new JLabel("Waiting for request.", JLabel.CENTER);
        this.statusLabel.setForeground(Color.white);
        this.statusLabel.setPreferredSize(new Dimension(100, 25));

        // Add in components
        this.add(this.floorLabel);
        this.add(this.slider);
        this.add(this.doorsLabel);
        this.add(this.statusLabel);
    }

    public void openDoors() {
        this.doorsLabel.setText("Doors open.");
    }

    public void closeDoors() {
        this.doorsLabel.setText("Doors closed.");
    }

    public void goToFloor(int floor) {
        this.slider.setValue(floor);
        this.floorLabel.setText(String.valueOf(floor));
    }

    public void updateStatus(String status) {
        this.statusLabel.setText(status);
    }

    public void setDirection(Direction direction) {
        this.floorLabel.setIcon(direction == Direction.Up ? UP_ICON : DOWN_ICON);
    }

}
