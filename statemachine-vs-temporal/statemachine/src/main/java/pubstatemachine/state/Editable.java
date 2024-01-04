package pubstatemachine.state;

import pubstatemachine.StateMonitor;
import pubstatemachine.message.AbstractMessage;
import pubstatemachine.message.MessageImpl;
import pubstatemachine.message.MessageType;
import pubstatemachine.model.Document;
import pubstatemachine.queue.SimpleMessageQueue;

public class Editable extends AbstractState {
  public Editable(SimpleMessageQueue queue) {
    super(queue);
  }

  public void enter() {
    System.out.println("Now in the Editable state");
  }

  @Override
  public void update(AbstractMessage message) throws InterruptedException {
    System.out.println(
        current.getClass().getSimpleName() + " is now updating " + message.getMessageType());
    Document document = message.getDocument();
    Thread.sleep(1000);
    queue.putMessage(new MessageImpl(MessageType.COMMAND_GRAPHIC_EDIT, document));
    queue.putMessage(new MessageImpl(MessageType.COMMAND_COPY_EDIT, document));
    StateMonitor sm = StateMonitor.getStateMonitor(message.getDocument());
    // Make sure there is a StateMonitor for this document. The StateMonitor will
    // is the mechanism for tracking the various states the document
    // has pass through in the of the document as it moves through the publication
    // process.
    if (sm == null) {
      throw new NullPointerException(
          String.format(
              "No State Monitor found in %s for update()", this.getClass().getSimpleName()));
    }
    sm.setEditable(true);
  }
}
