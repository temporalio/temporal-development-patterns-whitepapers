package publishingdemo;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import publishingdemo.model.Document;

@WorkflowInterface
public interface PublicationWorkflow {
  @WorkflowMethod
  void startWorkflow(Document document);
}
