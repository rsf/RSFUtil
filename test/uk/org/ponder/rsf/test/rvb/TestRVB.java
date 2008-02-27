/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.rvb;

import uk.org.ponder.rsf.bare.ActionResponse;
import uk.org.ponder.rsf.bare.RenderResponse;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;

/** Test for composite Resulting View Bindings issue RSF-59, reported in forums at
 * http://ponder.org.uk/rsf/posts/list/8.page
 */

public class TestRVB extends MultipleRSFTests {
  
  public TestRVB() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/rvb/rvb-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/rvb/rvb-application-context.xml");
  }
  
  public void testResultingViewBindings() {
    
    RenderResponse render = getRequestLauncher().renderView();
    UIForm form = (UIForm) render.viewWrapper.queryComponent(new UIForm());
    UICommand command = (UICommand) render.viewWrapper.queryComponent(new UICommand());
    
    ActionResponse result = getRequestLauncher().submitForm(form, command);
    ARIResult ariresult = result.ARIResult;
    
    assertTrue(ariresult.resultingView instanceof EntityCentredViewParameters);
    EntityCentredViewParameters ecvp = (EntityCentredViewParameters) ariresult.resultingView;
    // If the test is successful, the id of the "freshly created entity" now appears
    // in the outgoing view state.
    assertEquals(IDHolder.NEW_ID, ecvp.entity.ID);
  }
}
