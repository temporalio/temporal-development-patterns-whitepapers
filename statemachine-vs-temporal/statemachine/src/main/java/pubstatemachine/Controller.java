package pubstatemachine;

import pubstatemachine.message.AbstractMessage;
import pubstatemachine.queue.SimpleMessageQueue;
import pubstatemachine.state.*;

/**
 * The controller manages the state of the application based on the messages it receives. The
 * controller has a queue that it polls for messages built into the constructor. Messages are
 * processed within a switch statement based on the message type. The controller then updates the
 * state of the application based on the message type.
 */
public class Controller {

  private final SimpleMessageQueue queue = new SimpleMessageQueue();

  public Controller() {
    Thread pollThread =
        new Thread(
            () -> {
              // Declare the various states of the state machine
              AbstractState.inactive = new Inactive(queue);
              AbstractState.editable = new Editable(queue);
              AbstractState.graphicEdit = new GraphicEdit(queue);
              AbstractState.copyEdit = new CopyEdit(queue);
              AbstractState.awaitingEdits = new AwaitingEdits(queue);
              AbstractState.awaitingPublish = new AwaitingPublish(queue);
              AbstractState.publish = new Publish(queue);
              AbstractState.current = AbstractState.inactive;

              // Set up a continuous loop to poll the queue for messages and process them
              while (true) {
                try {
                  AbstractMessage msg = queue.getMessage();
                  System.out.println("Controller received message: " + msg.getMessageType());
                  switch (msg.getMessageType()) {
                    case EVENT_EDITABLE:
                      processEventEditable(msg);
                      break;
                    case EVENT_AWAIT_GRAPHIC_EDIT:
                    case EVENT_AWAIT_COPY_EDIT:
                      processEventAwaitEdits(msg);
                      break;
                    case EVENT_PUBLISHABLE:
                      processEventPublishable(msg);
                      break;
                    case EVENT_PUBLISHED:
                      System.out.println("Document published");
                      break;
                    case COMMAND_GRAPHIC_EDIT:
                      processCommandGraphicEdit(msg);
                      break;
                    case COMMAND_COPY_EDIT:
                      processCommandCopyEdit(msg);
                      break;
                    case COMMAND_PUBLISH:
                      processCommandPublish(msg);
                      break;
                    default:
                      throw new IllegalStateException("Unexpected value: " + msg.getMessageType());
                  }
                } catch (InterruptedException e) {
                  System.err.println("Polling interrupted: " + e.getMessage());
                  break;
                }
                try {
                  Thread.sleep(1000); // sleep for 1 second
                } catch (InterruptedException e) {
                  System.err.println("Sleep interrupted: " + e.getMessage());
                  break;
                }
              }
            });
    pollThread.start();
    System.out.println("Controller running");
  }

  // This method is called to send a message into the message queue
  public void sendMessage(AbstractMessage message) {
    System.out.println("Sending message: " + message.getMessageType());
    queue.putMessage(message);
  }

  // The following methods process messages received by the controller according to
  // the msg.getMessageType() value.
  private static void processEventPublishable(AbstractMessage message) throws InterruptedException {
    AbstractState.current = AbstractState.awaitingEdits;
    AbstractState.current.enter();
    StateMonitor sm = StateMonitor.getStateMonitor(message.getDocument());
    if (sm == null) {
      throw new NullPointerException("StateMonitor not found in processEventPublishable");
    }
    AbstractState.current.update(message);
  }

  private static void processEventEditable(AbstractMessage message) throws InterruptedException {
    AbstractState.current = AbstractState.editable;
    AbstractState.current.enter();
    StateMonitor sm = StateMonitor.getStateMonitor(message.getDocument());
    if (sm == null) {
      sm = new StateMonitor(message.getDocument());
      StateMonitor.addStateMonitor(sm);
    }
    if (!sm.isEditable()) {
      AbstractState.current.update(message);
    }
  }

  private static void processCommandGraphicEdit(AbstractMessage message)
      throws InterruptedException {
    AbstractState.current = AbstractState.graphicEdit;
    AbstractState.current.enter();
    StateMonitor sm = StateMonitor.getStateMonitor(message.getDocument());
    if (sm == null) {
      throw new NullPointerException("StateMonitor not found in processCommandGraphicEdit");
    }
    if (!sm.isGraphicEdited()) {
      AbstractState.current.update(message);
      sm.setGraphicEdited(true);
    }
  }

  private static void processCommandCopyEdit(AbstractMessage message) throws InterruptedException {
    AbstractState.current = AbstractState.copyEdit;
    AbstractState.current.enter();
    StateMonitor sm = StateMonitor.getStateMonitor(message.getDocument());
    if (sm == null) {
      throw new NullPointerException("StateMonitor not found in processCommandGraphicEdit");
    }
    if (!sm.isCopyEdited()) {
      AbstractState.current.update(message);
    }
  }

  private static void processEventAwaitEdits(AbstractMessage message) throws InterruptedException {
    AbstractState.current = AbstractState.awaitingEdits;
    AbstractState.current.enter();
    StateMonitor sm = StateMonitor.getStateMonitor(message.getDocument());
    if (sm == null) {
      throw new NullPointerException("StateMonitor not found in processEventAwaitEdits");
    }
    AbstractState.current.update(message);
  }

  private static void processCommandPublish(AbstractMessage message) throws InterruptedException {
    AbstractState.current = AbstractState.publish;
    AbstractState.current.enter();
    StateMonitor sm = StateMonitor.getStateMonitor(message.getDocument());
    if (sm == null) {
      throw new NullPointerException("StateMonitor not found in processEventAwaitEdits");
    }
    AbstractState.current.update(message);
  }
}
