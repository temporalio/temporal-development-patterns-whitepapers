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
  private boolean isEditable;
  private boolean isCopyEdited;
  private boolean isGraphicEdited;
  private boolean isPublishable;
  private boolean isPublished;

  static List<StateMonitor> stateMonitors = new ArrayList<>();

  public StateMonitor(Document document) {
    this.document = document;
  }

  public Document getDocument() {
    return document;
  }

  public boolean isEditable() {
    return isEditable;
  }

  public void setEditable(boolean editable) {
    isEditable = editable;
  }

  public boolean isCopyEdited() {
    return isCopyEdited;
  }

  public void setCopyEdited(boolean copyEdited) {
    isCopyEdited = copyEdited;
  }

  public boolean isGraphicEdited() {
    return isGraphicEdited;
  }

  public void setGraphicEdited(boolean graphicEdited) {
    isGraphicEdited = graphicEdited;
  }

  public boolean isPublishable() {
    return isPublishable;
  }

  public void setPublishable(boolean publishable) {
    isPublishable = publishable;
  }

  public boolean isPublished() {
    return isPublished;
  }

  public void setPublished(boolean published) {
    isPublished = published;
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
