package pubstatemachine.state;

import pubstatemachine.message.AbstractMessage;
import pubstatemachine.queue.SimpleMessageQueue;

public abstract class AbstractState {
  // add a constructor
  public AbstractState(SimpleMessageQueue queue) {
    AbstractState.queue = queue;
  }

  static SimpleMessageQueue queue;
  public static AbstractState inactive;
  public static AbstractState editable;
  public static AbstractState graphicEdit;
  public static AbstractState copyEdit;
  public static AbstractState awaitingEdits;
  public static AbstractState awaitingPublish;
  public static AbstractState publish;
  public static AbstractState current;

  public void enter() {}

  public void update(AbstractMessage message) throws InterruptedException {}
}
