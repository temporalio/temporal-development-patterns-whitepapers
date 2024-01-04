package pubstatemachine.message;

import pubstatemachine.model.Document;
public class MessageImpl extends AbstractMessage {
    public MessageImpl(MessageType messageType, Document document) {
        super(messageType, document);
    }
}
