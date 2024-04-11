//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
///**
// * Test suite to cover the functionality of the message queues.
// *
// * @author Matteo Golin, 101220709
// * @author Grant Achuzia, 101222695
// * @author Saja Fawagreh, 101217326
// * @author Javeria Sohail, 101197163
// * @author Yousef Hammad, 101217858
// * @version 0.0.0
// */
//class MessageQueueTest {
//
//    /**
//     * A message queue to be tested.
//     */
//    private MessageQueue<String> queue;
//
//    /**
//     * Creates a new message queue for each test.
//     */
//    @BeforeEach
//    void setUp() {
//        this.queue = new MessageQueue<>();
//    }
//
//    /**
//     * Tears down the tested message queue.
//     */
//    @AfterEach
//    void tearDown() {
//        this.queue = null;
//    }
//
//    /**
//     * Tests that adding a message to the queue updates the queue's empty status.
//     */
//    @Test
//    void testNotEmptyAfterPut() {
//        this.queue.putMessage("Test Message");
//        assertFalse(this.queue.isEmpty());
//    }
//
//    /**
//     * Tests that removing a message from the queue updates the queue's empty status.
//     */
//    @Test
//    void testEmptyAfterGet() {
//        this.queue.putMessage("Testing 123");
//        assertFalse(this.queue.isEmpty());
//        this.queue.getMessage();
//        assertTrue(this.queue.isEmpty());
//    }
//
//    /**
//     * Tests that a newly constructed message queue is empty.
//     */
//    @Test
//    void testOriginallyEmpty() {
//        assertTrue(this.queue.isEmpty());
//    }
//
//    /**
//     * Tests that a message put on the queue can be retrieved.
//     */
//    @Test
//    void testMessageRetrieval() {
//        String testMessage = "Test Message 2";
//        assertTrue(this.queue.isEmpty()); // Ensure that the message will be the only thing on the queue
//        this.queue.putMessage(testMessage);
//        assertEquals(testMessage, this.queue.getMessage());
//    }
//
//    /**
//     * Tests that the message queue is FIFO (First In, First Out).
//     */
//    @Test
//    void testQueueOrdering() {
//        assertTrue(this.queue.isEmpty());
//
//        String[] messages = {"1", "2", "3"};
//        for (String s : messages) {
//            this.queue.putMessage(s);
//        }
//
//        assertFalse(this.queue.isEmpty()); // There are now three messages on the queue
//
//        for (String s : messages) {
//            // Messages should be received in the same order they were placed
//            assertEquals(s, this.queue.getMessage());
//        }
//
//        assertTrue(this.queue.isEmpty());
//    }
//}