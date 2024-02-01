import java.util.ArrayList;

/**
 * Represents a simple message queue for communication between the Floor
 * subsystem and Elevator subsystem.
 * Messages can be put into and retrieved from separate queues for Floor and
 * Elevator messages.
 * This class ensures thread-safe operations for adding and retrieving messages.
 *
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public class MessageQueue<T> {

    // Queue for messages
    private ArrayList<T> messages;

    /**
     * Constructor for the MessageQueue class. Initializes the queues.
     */
    public MessageQueue() {
        this.messages = new ArrayList<T>();
    }

    /**
     * Puts a message into the queue.
     * Waits if the queue is not empty until it becomes empty.
     *
     * @param message The message to be added to the queue.
     */
    public synchronized void putMessage(T message) {
        this.messages.add(message);
        notifyAll();
    }

    /**
     * Retrieves a message from the queue.
     * Waits if the queue is empty until a message is available.
     *
     * @return The last message from the queue.
     */
    public synchronized T getMessage() {
        while (this.messages.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }

        notifyAll();
        return this.messages.removeLast();
    }
}
