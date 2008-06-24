/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.viewerror;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.bare.RenderResponse;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;

/** Test for composite Resulting View Bindings issue RSF-59, reported in forums at
 * http://ponder.org.uk/rsf/posts/list/8.page
 */

public class TestViewError extends MultipleRSFTests {
  
  public TestViewError() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/viewerror/viewerror-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/viewerror/viewerror-application-context.xml");
  }
  
  public void testResultingViewBindings() {
    
    RenderResponse render = getRequestLauncher().renderView();
    assertRenderError(render, true);
    TargettedMessageList list = (TargettedMessageList) render.requestContext.locateBean("targettedMessageList");
    assertEquals(1, list.size());
    TargettedMessage general = list.findGeneralError();
    assertNull(general);
  }
}
