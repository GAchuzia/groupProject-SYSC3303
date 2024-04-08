/**
 * This class encapsulates an ElevatorRequest to track its progression through the elevator system.
 * Specifically, it monitors whether the request has been picked up and dropped off at the designated floors.
 * The class provides methods to update and query the state of the request regarding these two critical events.
 */
public class RequestProgressWrapper {

    /** The elevator request being wrapped. */
    private ElevatorRequest request;

    /** Indicates whether the request has been picked up. */
    private boolean picked_up;

    /** Indicates whether the request has been dropped off. */
    private boolean dropped_off;

    /**
     * Constructs a new RequestProgressWrapper}around the provided ElevatorRequest, initializing
     * it with no progress (neither picked up nor dropped off).
     *
     * @param request The ElevatorRequest to be tracked for progress.
     */
    public RequestProgressWrapper(ElevatorRequest request) {
        this.request = request;
        this.picked_up = false;
        this.dropped_off = false;
    }

    /**
     * Checks if the wrapped request has been marked as picked up.
     *
     * @return true if the request was marked as picked up; false otherwise.
     */
    public boolean wasPickedUp() {
        return this.picked_up;
    }

    /**
     * Checks if the wrapped request has been marked as dropped off.
     *
     * @return true if the request was marked as dropped off; false otherwise.
     */
    public boolean wasDroppedOff() {
        return this.dropped_off;
    }

    /**
     * Determines if the wrapped request has completed its journey, meaning it has been both picked up
     * and dropped off at the intended floors.
     *
     * @return true if the request has been both picked up and dropped off; false otherwise.
     */
    public boolean isComplete() {
        return this.dropped_off; // Reflects the real completion status of the request.
    }

    /**
     * Provides access to the wrapped  ElevatorRequest.
     *
     * @return The underlying ElevatorRequest being tracked.
     */
    public ElevatorRequest getRequest() {
        return this.request;
    }

    /**
     * Updates the progress of the wrapped request based on a visited floor. The method marks the request as picked up
     * if the visited floor matches the origin floor and marks it as dropped off if the visited floor matches the
     * destination floor and the request has already been picked up.
     *
     * @param floor The floor number that was visited.
     */
    public void updateProgress(int floor) {
        // If the origin floor of the request has been visited, mark this request as picked up.
        if (this.request.getOriginFloor() == floor) {
            this.picked_up = true;
        }

        // If the destination floor of the request has been visited and the request has already been picked up,
        // mark this request as dropped off.
        if (this.request.getDestinationFloor() == floor && this.picked_up) {
            this.dropped_off = true;
            // The request is marked complete internally to reflect the true end-to-end completion.
            this.request.markComplete(); // This method needs to exist in ElevatorRequest to update its internal state.
        }
    }
}
