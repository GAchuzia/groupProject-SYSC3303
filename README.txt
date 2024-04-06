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

FloorSubsystem.java: Reads input data from a file and puts ElevatorRequests on its outgoing message queue. It listens
for messages on its incoming message queue which it prints to the console.
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
Saja Fawagreh: Updated the sequence and class diagram and the README.txt. Also added the scheduler algorithm.
Javeria Sohail: Updated the sequence and class diagram and the README.txt. Also added the scheduler algorithm
Grant Achuzia: Updated tests for ElevatorTest.java and ElevatorRequest.java. Comment out tests that used message queues.
Matteo Golin: Updated Scheduler, ElevatorSubsystem, and FloorSubsystem classes to communicate using UDP. Fixed
ElevatorRequest decoding bug.
Yousef Hammad: Worked on Elevator and ElevatorSubsystem.

Iteration 4:
Saja Fawagreh: Updated the sequence and class diagrams, as well as the state machines for the Elevator Subsystem and
Scheduler Subsystem, and the README.txt.
Javeria Sohail: Updated the sequence and class diagrams, as well as the state machines for the Elevator Subsystem and
Scheduler Subsystem, and the README.txt.
Grant Achuzia: Updated tests for the Elevator class.
Matteo Golin: Implemented the scheduler algorithm for responding to elevator shutdowns, and helped in the random fault
activation within the Elevator class.
Yousef Hammad: Updated the Elevator and the ElevatorState class

Iteration 5:
Matteo Golin: Modified GUI, wrote GUISubsystem and hooked it up to scheduler, created sequence and state machine
diagrams, implemented capacity limit.
Grant Achuzia:
Javeria Sohail:
Saja Fawagreh:
Yousef HammaD:

=== Setup Instructions ===

=== Running from the Terminal ===

To run the system from the terminal, you must first compile the entire project. A Makefile has been included, so typing
the command make all from within the project directory will compile all the required components.

To run each of the four required subsystems, you will need four terminal windows (one per subsystem). You may start them
using the command `java SubsystemName`, where `SubsystemName` is the filename of the subsystem’s corresponding Java file,
just without the `.java` extension. The file names are all listed above. Make sure to run the FloorSubsystem last,
although the other subsystems can be started in any order.

=== Running from within the IntelliJ IDE ===

Running from within the IntelliJ IDE is far simpler. Simply open the four files corresponding to the subsystems
mentioned above and click the green play button to compile and run them. Make sure that the Floor Subsystem is the last
to be run.
