package pubstatemachine;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import pubstatemachine.model.Document;

import java.net.MalformedURLException;
import java.net.URL;

/** Unit test for simple App. */
public class AppTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public AppTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  public void testStateMonitorList() throws MalformedURLException {
    URL url = new URL("https://learn.temporal.io/getting_started/#set-up-your-development-environment");
    Document doc1 = new Document(url);
    StateMonitor sm1 = new StateMonitor(doc1);
    sm1.setEditable(true);
    StateMonitor.addStateMonitor(sm1);

    URL url2 = new URL("https://www.zdnet.com/article/best-camera/");
    Document doc2 = new Document(url2);
    StateMonitor sm2 = new StateMonitor(doc2);
    sm2.setCopyEdited(true);
    StateMonitor.addStateMonitor(sm2);

    StateMonitor sm = StateMonitor.getStateMonitor(doc1);
    assertTrue(sm.isEditable());
    assertTrue(!sm.isCopyEdited());

    sm = StateMonitor.getStateMonitor(doc2);
    assertTrue(sm.isCopyEdited());
    assertTrue(!sm.isEditable());
  }
}
