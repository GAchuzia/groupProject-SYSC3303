=== SYSC 3303 Group 1 Project ===
Description: Establishing connections between three subsystems for an Elevator Simulator developed using Java.
Team Members: Matteo Golin, Yousef Hammad, Saja Fawagreh, Javeria Sohail, Grant Achuzia

=== Project Files ===
- ElevatorRequest.java: Specifies the structure for elevator requests
- ElevatorSubsystem.java: Represents the Elevator Subsystem's functionality (communicates with the scheduler and floor)
- FloorSubsytem.java: Represents the Floor Subsystem's functionality (read input data from a file and listens for messages from scheduler)
- Main.java: Initializes the threads used in the elevator simulator
- MessageQueue.java: Implements a message queue used for communicating between subsystems
- Scheduler.java: Assigns elevators to floors
- ClassUML.png: Diagram depicting the relationships between the varying classes in the elevator simulator
- SequenceUML.png: Diagram showing the sequential interaction between objects in the elevator simulator
- testdata.txt: Input file with data on the elevator's properties (time stamp, floor, button pressed, car button)
- README.txt: Text file describing the project's files and setup instructions

=== Group Responsibilities ===
Matteo Golin:
Yousef Hammad:
Saja Fawagreh:
Javeria Sohail: Wrote MessageQueues.java and made the UML Diagrams for Project Iteration 1
Grant Achuzia: Wrote JUnit test classes, testdata.txt, and README.txt for Project Iteration 1

=== Setup Instrcutions ===
Run `Main.java` to begin the elevator simulation
- Any messages will be displayed in the console
