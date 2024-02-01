import java.util.ArrayList;

/**
 * Represents a simple message queue for communication between the Floor
 * subsystem and Elevator subsystem.
 * Messages can be put into and retrieved from separate queues for Floor and
 * Elevator messages.
 * This class ensures thread-safe operations for adding and retrieving messages.
 *
 * @version 1.0
 * @author Javeria Sohail
 * @author Saja Fawagreh
 */
public class MessageQueue {

    // Queue for messages from the Floor subsystem.
    private ArrayList<String> floorMessages;

    // Queue for messages from the Elevator subsystem.
    private ArrayList<String> elevatorMessages;

    /**
     * Constructor for the MessageQueue class.
     * Initializes the queues and sets the initial empty flags to true.
     */
    public MessageQueue() {
        floorMessages = new ArrayList<>();
        elevatorMessages = new ArrayList<>();
    }

    /**
     * Puts a message into the Floor queue.
     * Waits if the queue is not empty until it becomes empty.
     *
     * @param message The message to be added to the Floor queue.
     */
    public synchronized void putFromFloor(String message) {
        while (!floorMessages.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }

        floorMessages.add(message);
        notifyAll();
    }

    /**
     * Retrieves a message from the Floor queue.
     * Waits if the queue is empty until a message is available.
     *
     * @return The last message from the Floor queue.
     */
    public synchronized String getFromFloor() {
        while (floorMessages.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return e.getMessage();
            }
        }

        String lastElement = floorMessages.getLast();
        floorMessages.removeLast();
        notifyAll();
        return lastElement;
    }

    /**
     * Puts a message into the Elevator queue.
     * Waits if the queue is not empty until it becomes empty.
     *
     * @param message The message to be added to the Elevator queue.
     */
    public synchronized void putFromElevator(String message) {
        while (!elevatorMessages.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }

        elevatorMessages.add(message);
        notifyAll();
    }

    /**
     * Retrieves a message from the Elevator queue.
     * Waits if the queue is empty until a message is available.
     *
     * @return The last message from the Elevator queue.
     */
    public synchronized String getFromElevator() {
        while (elevatorMessages.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return e.getMessage();
            }
        }

        String lastElement = elevatorMessages.getLast();
        elevatorMessages.removeLast();
        notifyAll();
        return lastElement;
    }
}
