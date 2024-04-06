import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ElevatorSlider extends JSlider {

    private Hashtable<Integer, JLabel> floorLabels;

    private static final Font LABEL_FONT = new Font("Sans Serif", Font.PLAIN, 20);

    public ElevatorSlider(int width, int height) {
        super(SwingConstants.VERTICAL, FloorSubsystem.GROUND_FLOOR, FloorSubsystem.NUM_FLOORS,
                FloorSubsystem.GROUND_FLOOR);

        Dimension size = new Dimension(width, height);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);

        this.setBackground(Color.BLACK);
        this.setPaintTicks(true);
        this.setPaintLabels(true);
        this.setMajorTickSpacing(height / FloorSubsystem.NUM_FLOORS);
        this.setSnapToTicks(true);
        this.setEnabled(false); // Slider should not be interactive

        // Create custom labels for each floor
        this.floorLabels = new Hashtable<>();
        for (int i = 1; i <= FloorSubsystem.NUM_FLOORS; i++) {
            JLabel label = new JLabel(String.valueOf(i));
            label.setForeground(Color.WHITE); // Use the passed color for the labels
            label.setFont(LABEL_FONT);
            this.floorLabels.put(i, label);
        }
        this.setLabelTable(this.floorLabels);
    }
}
