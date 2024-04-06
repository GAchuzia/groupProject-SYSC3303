import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ElevatorPanel extends JPanel {

    private static final int HEIGHT = 900;
    private static final int COLUMN_WIDTH = 300;

    /** Keeps track of the elevator columns for updating. */
    private ElevatorColumn columns[] = new ElevatorColumn[ElevatorSubsystem.NUM_ELEVATORS];

    public ElevatorPanel() {
        super();

        // 1 row and enough columns for however many elevators
        this.setLayout(new GridLayout(1, ElevatorSubsystem.NUM_ELEVATORS, 10, 10));
        this.setPreferredSize(new Dimension(COLUMN_WIDTH * ElevatorSubsystem.NUM_ELEVATORS, HEIGHT));
        this.setBackground(Color.black);

        // Create a panel for each elevator
        for (int i = 0; i < ElevatorSubsystem.NUM_ELEVATORS; i++) {
            this.columns[i] = new ElevatorColumn(COLUMN_WIDTH, HEIGHT);
            this.add(this.columns[i]);
        }
    }

    public void updateDisplay(ElevatorStatus status) {
        int i = status.getElevator();
        this.columns[i].goToFloor(status.getFloor());
        this.columns[i].setDirection(status.getDirection());
        this.columns[i].updateRiderCount(status.getRiders());
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
