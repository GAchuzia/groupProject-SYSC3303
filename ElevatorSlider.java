import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

/**
 * A custom slider component designed to visually represent the floors serviced by an elevator in a graphical user
 * interface. It extends JSlider to display floors vertically, from the ground floor to the top floor as
 * defined by the {@code FloorSubsystem} constants. The slider is non-interactive and serves purely as a visual
 * indicator of the elevator's current floor.
 * <p>
 * This component uses custom labels for each floor number, which can be styled separately from the slider's default
 * appearance. It is designed to be visually integrated into a larger GUI application that simulates elevator
 * operations.
 */
public class ElevatorSlider extends JSlider {

    /**
     * A hashtable mapping integer floor numbers to their corresponding JLabel components. This allows for
     * customized rendering of floor labels on the slider.
     */
    private Hashtable<Integer, JLabel> floorLabels;

    /**
     * The font used for the floor labels displayed alongside the slider. It is set to a standard sans-serif font
     * for clarity and readability.
     */
    private static final Font LABEL_FONT = new Font("Sans Serif", Font.PLAIN, 13);

    /**
     * Constructs a new ElevatorSlider with specified dimensions. It initializes the slider to span all
     * floors managed by the FloorSubsystem, sets up custom floor labels, and configures the slider's
     * appearance and behavior.
     *
     * @param width  The preferred width of the slider component.
     * @param height The preferred height of the slider component, which should accommodate the range of floors.
     */
    public ElevatorSlider(int width, int height) {
        // Initialize the slider to vertical orientation, setting its range based on the FloorSubsystem constants.
        super(SwingConstants.VERTICAL, FloorSubsystem.GROUND_FLOOR, FloorSubsystem.NUM_FLOORS, FloorSubsystem.GROUND_FLOOR);

        // Set the preferred size, background color, tick spacing, and enable label painting.
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setMajorTickSpacing(1);
        this.setPaintLabels(true);
        this.setPaintTicks(true);

        // Remove the default mouse listener to make the slider non-interactive and set it non-focusable.
        this.removeMouseListener(this.getMouseListeners()[0]);
        this.setFocusable(false);

        // Initialize the hashtable for custom floor labels.
        floorLabels = new Hashtable<>();

        // Populate the hashtable with JLabels for each floor, customizing their appearance.
        for (int i = FloorSubsystem.GROUND_FLOOR; i <= FloorSubsystem.NUM_FLOORS; i++) {
            JLabel label = new JLabel(String.valueOf(i));
            label.setForeground(Color.WHITE); // Set the text color to white for contrast.
            label.setPreferredSize(new Dimension(
                    label.getPreferredSize().width + 10, label.getPreferredSize().height));
            label.setFont(LABEL_FONT); // Apply the custom font.
            this.floorLabels.put(i, label); // Map the floor number to its label.
        }
        // Apply the custom labels to the slider.
        this.setLabelTable(floorLabels);
    }
}
