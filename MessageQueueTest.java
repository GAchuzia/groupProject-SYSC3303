import static org.junit.jupiter.api.Assertions.*;

class MessageQueueTest {

    @org.junit.jupiter.api.Test
    void putMessage() {
        // Make a message queue
        MessageQueue<String> messageQueue = new MessageQueue<>();
        messageQueue.putMessage("Test Message");
        // Check that message queue is not empty
        assertFalse(messageQueue.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getMessage() {
        // Make a message queue
        MessageQueue<String> messageQueue = new MessageQueue<>();
        String testMessage = "Test Message 2";

        messageQueue.putMessage(testMessage);
        String retrievedMessage = messageQueue.getMessage();

        // Check that testMessage and retrievedMessage are equal
        assertEquals(testMessage, retrievedMessage);
        // Makes sure queue is empty after message retrieval
        assertTrue(messageQueue.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
        // Make a message queue
        MessageQueue<String> messageQueue = new MessageQueue<>();
        // Checks if message queue is empty, returns true since no message was put in the queue
        assertTrue(messageQueue.isEmpty());

        // Make a second non-empty message queue
        MessageQueue<String> messageQueueTwo = new MessageQueue<>();
        messageQueueTwo.putMessage("I'm a string message!");
        // Checks if message queue is empty, returns false since a message was put in the queue
        assertFalse(messageQueueTwo.isEmpty());
    }
}