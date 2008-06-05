/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.vpi;

import uk.org.ponder.rsf.bare.RenderResponse;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/** Test for VPI ClassCastException RSF-63
 */

public class TestVPI extends MultipleRSFTests {
  
  public TestVPI() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/vpi/vpi-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/vpi/vpi-application-context.xml");
  }
   
  public void testVPI() {
    RenderResponse response = getRequestLauncher().renderView(new SimpleViewParameters(View1Producer.VIEW_ID));
    assertRenderError(response, false);
  }
}
