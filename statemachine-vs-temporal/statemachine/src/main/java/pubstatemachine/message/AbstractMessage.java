package pubstatemachine.message;

import pubstatemachine.model.Document;

public abstract class AbstractMessage {
  private final MessageType messageType;
  private final Document document;

  public AbstractMessage(MessageType messageType, Document document) {
    this.messageType = messageType;
    this.document = document;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public Document getDocument() {
    return document;
  }
}
