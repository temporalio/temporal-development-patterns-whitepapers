package publishingdemo;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import publishingdemo.model.Document;

public class PublishingActivitiesImpl implements PublishingActivities {
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
        logger.info(String.format(str, "Publish", document.getId(), document.getUrl(), "PUBLISH"));
    }
}