package publishingdemo;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.net.MalformedURLException;

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

  // Write a function that runs the code in App.java
  public void testMainOutput() throws MalformedURLException, InterruptedException {
    App.main(new String[] {});
  }
}
