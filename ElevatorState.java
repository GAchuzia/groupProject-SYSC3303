/**
 * Enumeration representing the states of an elevator.
 * The states include Idle, Moving, DoorsOpen, and DoorsClosed.
 *
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */
public enum ElevatorState {

    /** Represents the state when the elevator is not in use and is stationary. */
    Idle,

    /** Represents the state when the elevator is in motion. */
    Moving,

    /** Represents the state when the elevator doors are open. */
    DoorsOpen,

    /** Represents the state when the elevator doors are closed. */
    DoorsClosed,

    /**
     * Represents the state when the elevator encountered a non-recoverable fault
     * and is shut down.
     */
    Halted,
}
