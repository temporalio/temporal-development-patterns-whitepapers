package pubstatemachine;

import java.net.MalformedURLException;
import java.net.URL;
import pubstatemachine.message.AbstractMessage;
import pubstatemachine.message.MessageImpl;
import pubstatemachine.message.MessageType;
import pubstatemachine.model.Document;

/**
 * This is the client that sends a MessageType.EVENT_EDITABLE to the controller thus starting the
 * state machine.
 */
public class Client {
  public static void main(String[] args) throws InterruptedException, MalformedURLException {
    Controller controller = new Controller();
    URL url =
        new URL("https://learn.temporal.io/getting_started/#set-up-your-development-environment");
    Document document = new Document(url);
    AbstractMessage message = new MessageImpl(MessageType.EVENT_EDITABLE, document);
    System.out.println("Client: Sending event: " + message.getMessageType());

    controller.sendMessage(message);
    System.out.println("Client: Sent event to controller: " + message.getMessageType());
  }
}
