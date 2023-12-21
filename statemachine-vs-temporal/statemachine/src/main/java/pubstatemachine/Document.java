package pubstatemachine;

import java.net.URL;
import java.util.UUID;

/**
 * This class represents a document that will be published.
 */
public class Document {
    private URL url;
    private UUID id;

    public Document() {}

    public Document(URL url) {
        this.url = url;
        id = UUID.randomUUID();
    }

    public URL getUrl() {
        return url;
    }

    public UUID getId() {
        return id;
    }
}
