package pubstatemachine;

import java.util.ArrayList;
import java.util.List;
import pubstatemachine.model.Document;

/**
 * This class is used to monitor the state of a document as it moves throw the various states toward
 * completion of the publishing process.
 *
 * <p>It's used by the state machine to ensure that an event or command is processed only once.
 */
public class StateMonitor {
  private final Document document;
  private boolean Editable;
  private boolean CopyEdited;
  private boolean GraphicEdited;
  private boolean Publishable;
  private boolean Published;

  private static final List<StateMonitor> stateMonitors = new ArrayList<>();

  public StateMonitor(Document document) {
    this.document = document;
  }

  public Document getDocument() {
    return document;
  }

  public boolean isEditable() {
    return Editable;
  }

  public void setEditable(boolean editable) {
    Editable = editable;
  }

  public boolean isCopyEdited() {
    return CopyEdited;
  }

  public void setCopyEdited(boolean copyEdited) {
    CopyEdited = copyEdited;
  }

  public boolean isGraphicEdited() {
    return GraphicEdited;
  }

  public void setGraphicEdited(boolean graphicEdited) {
    GraphicEdited = graphicEdited;
  }

  public boolean isPublishable() {
    return Publishable;
  }

  public void setPublishable(boolean publishable) {
    Publishable = publishable;
  }

  public boolean isPublished() {
    return Published;
  }

  public void setPublished(boolean published) {
    Published = published;
  }

  public static void addStateMonitor(StateMonitor stateMonitor) {
    stateMonitors.add(stateMonitor);
  }

  public static void removeStateMonitor(StateMonitor stateMonitor) {
    stateMonitors.remove(stateMonitor);
  }

  public static StateMonitor getStateMonitor(Document document) {
    for (StateMonitor stateMonitor : stateMonitors) {
      if (stateMonitor.getDocument().equals(document)) {
        return stateMonitor;
      }
    }
    return null;
  }
}
