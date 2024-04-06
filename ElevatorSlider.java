import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ElevatorSlider extends JSlider {

    private Hashtable<Integer, JLabel> floorLabels;

    public ElevatorSlider() {
        super(SwingConstants.VERTICAL, 1, FloorSubsystem.NUM_FLOORS, 1);
        this.setPreferredSize(new Dimension(10, 200));
        this.setBackground(Color.darkGray); // Set the slider background to gray
        this.setPaintTicks(true);
        this.setPaintLabels(true);
        this.setMajorTickSpacing(1);
        this.setSnapToTicks(true);
        this.setEnabled(false); // Slider should not be interactive

        // Create custom labels for each floor
        this.floorLabels = new Hashtable<>();
        for (int i = 1; i <= FloorSubsystem.NUM_FLOORS; i++) {
            JLabel label = new JLabel(String.valueOf(i));
            label.setForeground(Color.WHITE); // Use the passed color for the labels
            this.floorLabels.put(i, label);
        }
        this.setLabelTable(this.floorLabels);
    }
}
