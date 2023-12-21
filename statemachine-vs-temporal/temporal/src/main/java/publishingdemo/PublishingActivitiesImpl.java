package publishingdemo;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import publishingdemo.model.Document;

public class PublishingActivitiesImpl implements PublishingActivities {

    private static boolean canPublishOnSunday = false;

    private static final Logger logger = Workflow.getLogger(PublishingActivitiesImpl.class);
    private final String str = "I am Amazing AI. I have the smarts to %s the document id: %s at URL %s. STARTING %s NOW!";

    @Override
    public void copyEdit(Document document) {
        logger.info(String.format(str, "Copy Edit", document.getId(), document.getUrl(), "COPY EDIT"));
    }

    @Override
    public void graphicEdit(Document document) {
        logger.info(String.format(str, "Graphic Edit", document.getId(), document.getUrl(), "GRAPHIC EDIT"));
    }

    @Override
    public void publish(Document document) {
        if(!canPublishOnSunday) throw new RuntimeException("Cannot publish on Sunday");
        logger.info(String.format(str, "Publish", document.getId(), document.getUrl(), "PUBLISH"));
    }

    /**
     *  This method is a one ring to rule them all. It is the compensation method for all activities.
     *  It's intended for demonstration purposes only.
     * @param activityName, the name of the activity to compensate
     * @param document, the Document that the activity is affecting
     */
    @Override
    public void compensate(String activityName, Document document) {
        logger.info(String.format("Compensating %s for document id: %s at URL %s", activityName, document.getId(), document.getUrl()));
        // Granted, compensation is  a bit terse here, but you get the idea.
        if(activityName.equals("publish")) canPublishOnSunday = true;
    }
}