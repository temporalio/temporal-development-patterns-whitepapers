package pubstatemachine.state;

import pubstatemachine.message.AbstractMessage;
import pubstatemachine.queue.SimpleMessageQueue;

public class AwaitingPublish extends AbstractState{
    public AwaitingPublish(SimpleMessageQueue queue) {
        super(queue);
    }

    public void enter(){
        System.out.println("Now in the AwaitingPublish state");
    }

    public void update(AbstractMessage message){
        System.out.println(message.getMessageType());;
    }
}
