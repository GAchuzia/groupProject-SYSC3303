//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/**
 * The scheduler that will be responsible for assigning the correct elevators to the correct floor.
 * @author Matteo Golin, 101220709
 * @author Grant Achuzia, 101222695
 * @author Saja Fawagreh, 101217326
 * @author Javeria Sohail, 101197163
 * @author Yousef Hammad, 101217858
 * @version 0.0.0
 */

public class Scheduler implements Runnable {

    private MessageQueue messageQ;

    public Scheduler (MessageQueue messageQ){
        this.messageQ = messageQ;
    }
    @Override
    public void run() {
        //NOTE: Need to create floorMessagesSize and elevatorMessagesSize methods in MessageQueue
        while (messageQ.floorMessagesSize() > 0 || messageQ.elevatorMessagesSize() > 0){
            //NOTE: need to create checkFloor methods in MessageQueue
            if (messageQ.checkFloor()){
                String floorMessage = messageQ.getFromFloor();
                messageQ.putFromElevator(floorMessage);
            }
            //NOTE: need to create checkEelevator method in messageQueue
            if (messageQ.checkEelevator()){
                String elevatorMessage = messageQ.getFromElevator();
                messageQ.putFromFloor(elevatorMessage);
            }
        }
    }
}
