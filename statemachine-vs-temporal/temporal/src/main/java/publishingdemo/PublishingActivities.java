package publishingdemo;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import publishingdemo.model.Document;

@ActivityInterface
public interface PublishingActivities {
  @ActivityMethod
  void copyEdit(Document document);

  @ActivityMethod
  void graphicEdit(Document document);

  @ActivityMethod
  void publish(Document document);

  @ActivityMethod
  void compensate(String activityName, Document document);
}