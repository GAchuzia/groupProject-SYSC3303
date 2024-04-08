import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

/**
 * The ElevatorPanel class manages and displays a panel for each elevator in the system.
 * It is responsible for updating the state of each elevator column based on the status received.
 */
public class ElevatorPanel extends JPanel {

    private static final int HEIGHT = 700; // The height of each elevator column panel
    private static final int COLUMN_WIDTH = 300; // The width of each elevator column panel

    /**
     * Array holding the ElevatorColumn objects for managing the display of each elevator's status.
     */
    private ElevatorColumn[] columns = new ElevatorColumn[ElevatorSubsystem.NUM_ELEVATORS];

    /**
     * Constructs an ElevatorPanel with a specific layout based on the number of elevators.
     * Initializes and adds a column for each elevator to the panel.
     */
    public ElevatorPanel() {
        super();

        // Defines a grid layout with a single row and a number of columns equal to the number of elevators
        GridLayout layout = new GridLayout(1, ElevatorSubsystem.NUM_ELEVATORS, 10, 10);
        this.setLayout(layout);
        this.setPreferredSize(new Dimension(COLUMN_WIDTH * ElevatorSubsystem.NUM_ELEVATORS, HEIGHT));
        this.setBackground(Color.black);

        // Initialize and add an ElevatorColumn for each elevator
        for (int i = 0; i < ElevatorSubsystem.NUM_ELEVATORS; i++) {
            this.columns[i] = new ElevatorColumn(i, COLUMN_WIDTH, HEIGHT - 20);
            this.add(this.columns[i]);
        }
    }

    /**
     * Updates the display of a specific elevator based on its current status.
     * This includes the elevator's floor, number of riders, direction, door status,
     * and handling any faults or shutdown states.
     *
     * @param status The current status of an elevator.
     */
    public void updateDisplay(ElevatorStatus status) {
        int i = status.getElevator();
        this.columns[i].goToFloor(status.getFloor());
        this.columns[i].updateRiderCount(status.getRiders());
        this.columns[i].setDirection(status.getDirection());

        if(status.getDoor() >= 0) {
            this.columns[i].setDoor(status.getDoor() == 1);
        } else {
            this.columns[i].handleFault(status.getDoor(), status.getFloor());
        }

        if(status.isComplete()) {
            this.columns[i].unHighlightDestination(status.getDestinationFloor());
            this.columns[i].removeStar(status.getOriginFloor());
        } else {
            if(status.getDestinationFloor() != 0 && status.getOriginFloor() != 0) {
                this.columns[i].highlightDestination(status.getDestinationFloor());
                this.columns[i].addStar(status.getOriginFloor(), status.getDestinationFloor());
            }
        }

        if (status.isShutDown()) {
            this.columns[i].shutDown();
        }
    }
}

/**
 * Demonstrates the use of the ElevatorPanel in a simple Swing application.
 */
class ElevatorPanelDemo {

    /**
     * The entry point of the application. Sets up the GUI on the Event Dispatch Thread.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ElevatorPanelDemo::createAndShowGUI);
    }

    /**
     * Initializes the GUI components and displays the main frame.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Elevator Panel Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ElevatorPanel elevatorPanel = new ElevatorPanel();
        frame.add(elevatorPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window when displayed
        frame.setVisible(true);
    }
}
