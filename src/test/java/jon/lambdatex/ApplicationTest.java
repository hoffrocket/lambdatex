package jon.lambdatex;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * User: jon
 * Date: 4/1/17
 * Time: 11:44 AM
 */
public class ApplicationTest {
  @Test
  public void handleBasic() {
    Application app = new Application();
    GatewayResponse response = app.handleRequest(Collections.singletonMap("queryStringParameters", Collections.singletonMap("q","e=mc^2")), null);
    Assert.assertEquals(200, response.getStatusCode());
  }

  @Test
  public void handleBadParams() {
    Application app = new Application();
    GatewayResponse response = app.handleRequest(Collections.emptyMap(), null);
    Assert.assertEquals(400, response.getStatusCode());
  }

  @Test
  public void handleBadFormula() {
    Application app = new Application();
    GatewayResponse response = app.handleRequest(Collections.singletonMap("queryStringParameters", Collections.singletonMap("q","e &= mc^2")), null);
    Assert.assertEquals(400, response.getStatusCode());
  }


  public static void main(String [] args) {
    Application app = new Application();
    int loop = 100;
    long startTime = System.nanoTime();
    for (int i = 0; i < loop; i++) {
      app.handleRequest(Collections.singletonMap("path","/100/\\sin A \\cos B = \\frac{1}{2}\\left[ \\sin(A-B)+\\sin(A+B) \\right]"), null);
    }
    long elapsedNanos = System.nanoTime() - startTime;
    System.out.println("Time per handle " + (elapsedNanos/loop/1000) + "µs");
  }
}
