/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.cancellation;

import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.bare.ActionResponse;
import uk.org.ponder.rsf.bare.RenderResponse;
import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Test for request cancellation issue http://www.caret.cam.ac.uk/jira/browse/RSF-27
 */

public class TestCancellation extends MultipleRSFTests {
  
  public TestCancellation() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/cancellation/cancellation-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/cancellation/cancellation-application-context.xml");
  }
  
  public void testCancellation() {
    
    RenderResponse render = getRequestLauncher().renderView();
    UIForm form = (UIForm) render.viewWrapper.queryComponent(new UIForm());
    UICommand command = (UICommand) render.viewWrapper.queryComponent(new UICommand());
    
    ActionResponse response = getRequestLauncher().submitForm(form, command);
    
    assertActionError(response, true);
    
    RequestWrapper wrapper = (RequestWrapper) response.requestContext.locateBean("requestWrapper");
    assertTrue(wrapper.cancelled);
    
    TargettedMessageList tml = (TargettedMessageList) response.requestContext.locateBean("targettedMessageList");
    assertEquals(1, tml.size());
    
    ARIResult result = response.ARIResult;
    assertTrue(result.resultingView instanceof ViewParameters);
    ViewParameters resultingView = (ViewParameters) result.resultingView;
    assertEquals(RequestLauncher.TEST_VIEW, resultingView.viewID);
  }
}
