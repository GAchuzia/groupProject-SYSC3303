import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ElevatorPanel extends JPanel {
	private JLabel floorLabel, doorsLabel, statusLabel;
	private JSlider elevatorSlider;
	private static final int NUM_FLOORS = 8; // Assuming 8 floors as in the test file

	// Icons and text constants
	private static final ImageIcon UP_ICON = new ImageIcon("Icons/upArrowIcon.png");
	private static final ImageIcon DOWN_ICON = new ImageIcon("Icons/downArrowIcon.png");
	private static final String DOORS_OPEN = "[|   |]";
	private static final String DOORS_CLOSED = "[|]";

	public ElevatorPanel() {
		setLayout(new GridLayout(1, 3, 10, 10)); // 1 row, 3 columns for 3 elevators
		setPreferredSize(new Dimension(375, 125));
		setBackground(Color.black);

		initializeComponents();
	}

	private void initializeComponents() {
		// Create a panel for each elevator
		for (int i = 0; i < 3; i++) {
			JPanel elevatorColumn = new JPanel(new GridLayout(4, 1)); // 4 rows, 1 column
			elevatorColumn.setBackground(Color.black);
			floorLabel = createLabel("0", UP_ICON); // Example initialization with UP_ICON

			// Change color based on elevator index
			Color labelColor = (i % 3 == 0) ? new Color(255, 100, 100) :
					(i % 3 == 1) ? new Color(100, 255, 100) :
							new Color(100, 100, 255);

			elevatorSlider = createElevatorSlider(NUM_FLOORS, 1, labelColor); // Floors 1 to NUM_FLOORS
			doorsLabel = createLabel(DOORS_OPEN, null);
			statusLabel = createLabel("Waiting for request", null);

			elevatorColumn.add(floorLabel);
			elevatorColumn.add(elevatorSlider);
			elevatorColumn.add(doorsLabel);
			elevatorColumn.add(statusLabel);

			this.add(elevatorColumn);
		}
	}

	private JLabel createLabel(String text, ImageIcon icon) {
		JLabel label = new JLabel(text, JLabel.CENTER);
		label.setForeground(Color.white);
		label.setIcon(icon);
		label.setPreferredSize(new Dimension(100, 25));
		return label;
	}

	private JSlider createElevatorSlider(int max, int min, Color labelColor) {
		JSlider slider = new JSlider(SwingConstants.VERTICAL, min, max, min);
		slider.setPreferredSize(new Dimension(10, 100));
		slider.setBackground(Color.darkGray); // Set the slider background to gray
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(1);
		slider.setSnapToTicks(true);
		slider.setEnabled(false); // Slider should not be interactive

		// Create custom labels for each floor
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		for (int i = min; i <= max; i++) {
			JLabel label = new JLabel(String.valueOf(i));
			label.setForeground(labelColor); // Use the passed color for the labels
			labelTable.put(i, label);
		}
		slider.setLabelTable(labelTable);

		return slider;
	}

	// Methods to change the state of the elevator panel
	// TODO: Link with GUI subsystem made my Matteo Golin
	public void openDoors() {
		doorsLabel.setText(DOORS_OPEN);
	}

	// TODO: Link with GUI subsystem made my Matteo Golin
	public void closeDoors() {
		doorsLabel.setText(DOORS_CLOSED);
	}

	// TODO: Link with GUI subsystem made my Matteo Golin
	public void goToFloor(int floor) {
		elevatorSlider.setValue(floor);
		floorLabel.setText(String.valueOf(floor));
	}

	// TODO: Link with GUI subsystem made my Matteo Golin
	public void updateStatus(String status) {
		statusLabel.setText(status);
	}

	// TODO: Link with GUI subsystem made my Matteo Golin
	public void setDirection(boolean goingUp) {
		floorLabel.setIcon(goingUp ? UP_ICON : DOWN_ICON);
	}
}

class ElevatorPanelDemo {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(ElevatorPanelDemo::createAndShowGUI);
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Elevator Panel Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ElevatorPanel elevatorPanel = new ElevatorPanel();
		frame.add(elevatorPanel);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
