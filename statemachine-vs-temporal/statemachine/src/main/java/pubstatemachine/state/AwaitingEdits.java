package pubstatemachine.state;

import pubstatemachine.model.Document;
import pubstatemachine.StateMonitor;
import pubstatemachine.message.AbstractMessage;
import pubstatemachine.message.MessageImpl;
import pubstatemachine.message.MessageType;
import pubstatemachine.queue.SimpleMessageQueue;

public class AwaitingEdits extends AbstractState {
    public AwaitingEdits(SimpleMessageQueue queue) {
        super(queue);
    }

    public void enter() {
        System.out.println("Now in the AwaitingEdits state");
    }

    public void update(AbstractMessage message) throws InterruptedException {
        System.out.println(current.getClass().getSimpleName() + " is now updating " + message.getMessageType());
        Document document = message.getDocument();
        Thread.sleep(1000);
        StateMonitor sm = StateMonitor.getStateMonitor(message.getDocument());
        if (sm == null) {
            throw new NullPointerException(String.format("No State Monitor found in %s for update()", this.getClass().getSimpleName()));
        }

        if (message.getMessageType() == MessageType.EVENT_AWAIT_COPY_EDIT) {
            System.out.println("Doing copy edit behavior");
            if (!sm.isCopyEdited()) {
                queue.putMessage(new MessageImpl(MessageType.EVENT_AWAIT_COPY_EDIT, document));
            }
        }

        if (message.getMessageType() == MessageType.EVENT_AWAIT_GRAPHIC_EDIT) {
            System.out.println("Doing graphic edit behavior");
            if (!sm.isGraphicEdited()) {
                queue.putMessage(new MessageImpl(MessageType.EVENT_AWAIT_GRAPHIC_EDIT, document));
            }
        }

        if (sm.isGraphicEdited() && sm.isCopyEdited() && !sm.isPublishable()) {
            queue.putMessage(new MessageImpl(MessageType.COMMAND_PUBLISH, document));
            sm.setPublishable(true);
        }
    }
}
