package pubstatemachine.queue;

import pubstatemachine.message.AbstractMessage;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is the basic message queue that's used as the message
 * transport for the state machine.
 */
public class SimpleMessageQueue {

    private final LinkedBlockingQueue<AbstractMessage> queue;

    public SimpleMessageQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void putMessage(AbstractMessage message) {
        queue.offer(message);
    }

    public AbstractMessage getMessage() throws InterruptedException {
        return queue.take();
    }
}