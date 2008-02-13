/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.diminish;

import uk.org.ponder.rsf.bare.ActionResponse;
import uk.org.ponder.rsf.bare.RenderResponse;
import uk.org.ponder.rsf.bare.ViewWrapper;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Test for "diminishing application of values" issue RSF-40
 */

public class TestDiminish extends MultipleRSFTests {
  
  public TestDiminish() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/diminish/diminish-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/diminish/diminish-application-context.xml");
  }
  
  private static class View {
    public UIForm form;
    public UIInput int1;
    public UIInput int2;
    public UIInput int3;
  }
  
  private View queryView(ViewWrapper wrapper) {
    View togo = new View();
    togo.form = (UIForm) wrapper.queryComponent(new UIForm());
    UIInput iq = new UIInput();
    iq.ID = "int1";
    togo.int1 = (UIInput) wrapper.queryComponent(iq);
    iq.ID = "int2";
    togo.int2 = (UIInput) wrapper.queryComponent(iq);
    iq.ID = "int3";
    togo.int3 = (UIInput) wrapper.queryComponent(iq);
    return togo;
  }
  
  public void testDiminishment() {
    RenderResponse response = getRequestLauncher().renderView();
    
    View view = queryView(response.viewWrapper);
    view.int1.setValue("1");
    view.int2.setValue("2");
    view.int3.setValue("3");
   
    // Submission 1 - should be rejected, and result in all 3 values being returned
    // to the view
    ActionResponse result = getRequestLauncher().submitForm(view.form, null);
    ARIResult ariresult = result.ARIResult;
   
    RenderResponse response2 = getRequestLauncher().renderView((ViewParameters) ariresult.resultingView);
    View view2 = queryView(response2.viewWrapper);
    
    assertEquals(view2.int1.getValue(), "1");
    assertEquals(view2.int2.getValue(), "2");
    assertEquals(view2.int3.getValue(), "3");
    
    view2.int1.setValue("2");
    view2.int2.setValue("3");

    // Submission 2 - should be rejected, and result in all 3 values being returned
    // to the view. However due to RSF-40, the value of int3 will not on this occasion
    // hit the backing model, even though it differs from the original value
    
    ActionResponse result2 = getRequestLauncher().submitForm(view2.form, null);
    Diminisher diminish = (Diminisher) result2.requestContext.locateBean("diminisher");
    assertEquals(new Integer("2"), diminish.int1);
    assertEquals(new Integer("3"), diminish.int2);
    assertEquals(new Integer("3"), diminish.int3);
    
    ARIResult ariresult2 = result2.ARIResult;
    
    // Rendering this back to the user should still show 2, 3, 3
    RenderResponse response3 = getRequestLauncher().renderView((ViewParameters) ariresult2.resultingView);
    View view3 = queryView(response3.viewWrapper);
    
    view3.int1.setValue("0");

    // This application should be successful
    ActionResponse result3 = getRequestLauncher().submitForm(view3.form, null);
    Diminisher diminish2 = (Diminisher) result3.requestContext.locateBean("diminisher");    
    assertEquals(null, diminish2.int1);
    assertEquals(new Integer("3"), diminish2.int2);
    assertEquals(new Integer("3"), diminish2.int3);
  }
}
