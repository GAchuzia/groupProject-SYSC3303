=== SYSC 3303 Group 1 Project ===

Establishing synchronized communication between three subsystems for an Elevator Simulator developed in Java.

=== Team Members ===

- Matteo Golin
- Yousef Hammad
- Saja Fawagreh
- Javeria Sohail
- Grant Achuzia

=== Project Files ===

Elevator.java: Represents a physical or simulated elevator, providing functionality to simulate its behavior.
ElevatorTest.java: Test suite to verify the functionality of the Elevator class.

ElevatorRequest.java: Specifies the structure for elevator requests (sample input in the testdata.txt class).
ElevatorRequestTest.java: Test suite to verify the functionality of the ElevatorRequest class.

Direction.java: Contains a single enum which represents possible directions for the elevator to travel in (Up/Down).

ElevatorSubsystem.java: Listens for messages on its incoming message queue, prints them to the console and echoes them
on its outgoing queue.
ElevatorSubsystemTest.java: Test suite to verify the functionality of the ElevatorSubsystem class.

FloorSubsystem.java: Reads input data from a file and puts ElevatorRequests on its outgoing message queue. It listens for
messages on its incoming message queue which it prints to the console.
FloorSubsystemTest.java: Test suite to verify the functionality of the FloorSubsystem class.

MessageQueue.java: Implements a message queue used for synchronized communication between subsystems/threads.
MessageQueueTest.java: Test suite to verify the functionality of the MessageQueue class.

Scheduler.java: Forwards messages from the FloorSubsystem to the ElevatorSubsystem, and vice versa.
SchedulerTest.java: Test suite to verify the functionality of the Scheduler class.

ElevatorState.java: Represents the state of the elevator (Idle, Moving, DoorsOpen, DoorsClosed).

ClassUML.png: Diagram depicting the relationships between the varying classes in the elevator simulator.

SequenceUML.png: Diagram showing the sequential interaction between objects in the elevator simulator.

SequenceUML.puml: Open-source UML-based component used in making the sequence diagram.

StateMachine.png: State machine diagram depicting the states and transitions of the elevator system.

testdata.txt: Input file with data on the elevator's properties (time stamp, floor, button pressed, car button).

=== Group Responsibilities ===

Iteration 1:

Matteo Golin: Implemented JUnit testing, performed integration tests, wrote MessageQueue, wrote FloorSubsystem and
helped debug other classes.
Yousef Hammad: Wrote Scheduler and helped combine the other java classes together for Project Iteration 1.
Saja Fawagreh: Wrote MessageQueue and created the UML diagrams for Project Iteration 1.
Javeria Sohail: Wrote MessageQueue and made the UML Diagrams for Project Iteration 1.
Grant Achuzia: Wrote JUnit test classes, testdata.txt, and README.txt for Project Iteration 1.

Iteration 2:

Saja Fawagreh: Updated the sequence diagram and the README.txt.
Javeria Sohail: Updated the sequence diagram and the README.txt.
Grant Achuzia: Updated the ElevatorSubsystemTest file to work with 1 elevator. Also added a test class for Elevator.java
Matteo Golin: Wrote the state machine code for the Elevator and Scheduler classes, and helped add a test case for the
ElevatorTest.
Yousef Hammad: Developed the scheduler and elevator subsystem state machine diagrams

Iteration 3:
Saja Fawagreh: Updated the sequence and class diagram and the README.txt.
Javeria Sohail: Updated the sequence and class diagram and the README.txt.
Grant Achuzia: Update tests for BLANK methods.
Matteo Golin:

=== Setup Instructions ===

Compile all Java files and use the command `java Main` to run the main project. You can also run Main.java from the
IntelliJ IDE.
To run tests, you can run any individual test in the IntelliJ IDE using the JUnit integration it provides.
