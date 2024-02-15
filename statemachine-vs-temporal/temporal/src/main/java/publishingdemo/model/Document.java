package publishingdemo.model;

import java.net.URL;
import java.util.UUID;

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
