=== SYSC 3303 Group 1 Project ===

Establishing synchronized communication between three subsystems for an Elevator Simulator developed in Java.

=== Team Members ===

- Matteo Golin
- Yousef Hammad
- Saja Fawagreh
- Javeria Sohail
- Grant Achuzia

=== Project Files ===

ElevatorRequest.java: Specifies the structure for elevator requests (sample input in the testdata.txt class).
ElevatorRequestTest.java: Test suite to verify the functionality of the ElevatorRequest class.

Direction.java: Contains a single enum which represents possible directions for the elevator to travel in (Up/Down).

ElevatorSubsystem.java: Listens for messages on its incoming message queue, prints them to the console and echoes them
on its outgoing queue.
ElevatorSubsystemTest.java: Test suite to verify the functionality of the ElevatorSubsystem class.

FloorSubsytem.java: Reads input data from a file and puts ElevatorRequests on its outgoing message queue. It listens for
messages on its incoming message queue which it prints to the console.
FloorSubsystemTest.java: Test suite to verify the functionality of the FloorSubsystem class.

MessageQueue.java: Implements a message queue used for synchronized communication between subsystems/threads.
MessageQueueTest.java: Test suite to verify the functionality of the MessageQueue class.

Scheduler.java: Forwards messages from the FloorSubsystem to the ElevatorSubsystem, and vice versa.
SchedulerTest.java: Test suite to verify the functionality of the Scheduler class.

Main.java: Initializes each subsystem into a separate thread and runs the simulator. Currently messages are generated
from the FloorSubsystem using the testdata.txt file, and passed along through the Scheduler to the ElevatorSubsystem
before being passed back again.

ClassUML.png: Diagram depicting the relationships between the varying classes in the elevator simulator.

SequenceUML.png: Diagram showing the sequential interaction between objects in the elevator simulator.

testdata.txt: Input file with data on the elevator's properties (time stamp, floor, button pressed, car button).

=== Group Responsibilities ===

Matteo Golin: Implemented JUnit testing, performed integration tests, wrote MessageQueue, wrote FloorSubsystem and
helped debug other classes.
Yousef Hammad: Wrote Scheduler and helped combine the other java classes together for Project Iteration 1.
Saja Fawagreh: Wrote MessageQueue and created the UML diagrams for Project Iteration 1.
Javeria Sohail: Wrote MessageQueue and made the UML Diagrams for Project Iteration 1.
Grant Achuzia: Wrote JUnit test classes, testdata.txt, and README.txt for Project Iteration 1.

=== Setup Instructions ===

Compile all Java files and use the command `java Main` to run the main project. You can also run Main.java from the
IntelliJ IDE.
To run tests, you can run any individual test in the IntelliJ IDE using the JUnit integration it provides.
