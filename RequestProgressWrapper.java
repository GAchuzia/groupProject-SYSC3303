/**
 * This class tracks the progress of a request as the elevator visits the
 * required floors.
 */
public class RequestProgressWrapper {

    private ElevatorRequest request;

    private boolean picked_up;

    private boolean dropped_off;

    /**
     * Constructs a new wrapper around the provided request with no progress yet.
     * 
     * @param request The request to wrap.
     */
    public RequestProgressWrapper(ElevatorRequest request) {
        this.request = request;
        this.picked_up = false;
        this.dropped_off = false;
    }

    /**
     * Checks whether or not this request was picked up.
     * 
     * @return True if the request was marked as picked up, false otherwise.
     */
    public boolean wasPickedUp() {
        return this.picked_up;
    }

    /**
     * Checks whether or not this request was dropped off.
     * 
     * @return True if the request was marked as a dropped off.
     */
    public boolean wasDroppedOff() {
        return this.dropped_off;
    }

    /**
     * Checks whether or not this request has had both pick-up and drop-off
     * completed.
     * 
     * @return True if the request has had both pick-up and drop-off locations
     *         visited, false otherwise.
     */
    public boolean isComplete() {
        return this.request.isComplete();
    }

    /**
     * Provides access to the underlying request in this wrapper.
     * 
     * @return The request this wrapper contains.
     */
    public ElevatorRequest getRequest() {
        return this.request;
    }

    /**
     * Updates the progress of this request by marking the pick up or drop-off
     * complete based on the knowledge that the
     * passed floor was visited.
     *
     * If the request has not been marked with pick up complete yet, it will not
     * mark the drop off as complete even if
     * the passed floor matches the drop-off destination.
     *
     * @param floor The floor that was visited to check against the request.
     */
    public void updateProgress(int floor) {

        // If the origin floor of the request has been visited, mark this request as
        // picked up
        if (this.request.getOriginFloor() == floor) {
            this.picked_up = true;
        }

        // If the destination floor of the request has been visited, AND the origin
        // floor has already been visited, mark
        // this request as dropped off
        if (this.request.getDestinationFloor() == floor && this.picked_up) {
            this.dropped_off = true;
            this.request.markComplete(); // Pick up and drop off have been achieved
        }
    }
}
