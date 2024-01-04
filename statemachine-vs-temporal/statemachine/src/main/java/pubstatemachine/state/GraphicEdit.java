package pubstatemachine.state;

import pubstatemachine.model.Document;
import pubstatemachine.StateMonitor;
import pubstatemachine.message.AbstractMessage;
import pubstatemachine.message.MessageImpl;
import pubstatemachine.message.MessageType;
import pubstatemachine.queue.SimpleMessageQueue;

public class GraphicEdit extends AbstractState {
    public GraphicEdit(SimpleMessageQueue queue) {
        super(queue);
    }

    public void enter() {
        System.out.println("Now in the GraphicEdit state");
    }

    public void update(AbstractMessage message) throws InterruptedException {
        System.out.println(current.getClass().getSimpleName() + " is now updating " + message.getMessageType());
        Document document = message.getDocument();
        Thread.sleep(1000);
        StateMonitor sm = StateMonitor.getStateMonitor(message.getDocument());
        if(sm == null) {
            throw new NullPointerException(String.format("No State Monitor found in %s for update()",this.getClass().getSimpleName()));
        }
        if(!sm.isGraphicEdited()){
            sm.setGraphicEdited(true);
            queue.putMessage(new MessageImpl(MessageType.EVENT_AWAIT_COPY_EDIT, document));
        }


    }
}
