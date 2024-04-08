import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ElevatorSlider extends JSlider {

    private Hashtable<Integer, JLabel> floorLabels;

    private static final Font LABEL_FONT = new Font("Sans Serif", 0, 13);

    public ElevatorSlider(int width, int height) {
        super(SwingConstants.VERTICAL, FloorSubsystem.GROUND_FLOOR, FloorSubsystem.NUM_FLOORS,
                FloorSubsystem.GROUND_FLOOR);

        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setMajorTickSpacing(1);
        this.setPaintLabels(true);
        this.setPaintTicks(true);

        this.removeMouseListener(this.getMouseListeners()[0]);
        this.setFocusable(false);

        floorLabels = new Hashtable<>();

        for (int i = FloorSubsystem.GROUND_FLOOR; i <= FloorSubsystem.NUM_FLOORS; i++) {
            JLabel label = new JLabel(String.valueOf(i));
            label.setForeground(Color.WHITE);
            label.setPreferredSize(new Dimension(
                    label.getPreferredSize().width + 10, label.getPreferredSize().height));
            label.setFont(LABEL_FONT);
            this.floorLabels.put(i, label);
        }
        this.setLabelTable(floorLabels);
    }
}
