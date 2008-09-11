/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.data;

import uk.org.ponder.rsf.bare.ActionResponse;
import uk.org.ponder.rsf.bare.RenderResponse;
import uk.org.ponder.rsf.bare.ViewWrapper;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.test.data.producers.LogonProducer;
import uk.org.ponder.rsf.test.data.producers.ParamsDataView;
import uk.org.ponder.rsf.test.data.producers.UserDataView;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Test for interaction of Data Views together with form submissions and other
 * issues from http://ponder.org.uk/rsf/posts/list/251.page
 */

public class TestData extends MultipleRSFTests {
  
  public TestData() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/data/data-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/data/data-application-context.xml");
  }
  
  private static class LogonForm {
    public UIForm form;
    public UICommand command;
    public UIInput name;
    public UIInput password;
  }
  
  private LogonForm queryForm(ViewWrapper wrapper) {
    LogonForm togo = new LogonForm();
    togo.form = (UIForm) wrapper.queryComponent(new UIForm());
    UIInput iq = new UIInput();
    iq.ID = "login-user";
    togo.name = (UIInput) wrapper.queryComponent(iq);
    iq.ID = "login-password";
    togo.password = (UIInput) wrapper.queryComponent(iq);

    return togo;
  }
  
  public ActionResponse tryLogon(String name, String password) {
    ViewParameters logon = new SimpleViewParameters(LogonProducer.VIEW_ID);
    RenderResponse render = getRequestLauncher().renderView(logon);
  
    LogonForm form = queryForm(render.viewWrapper);
    form.name.setValue(name);
    form.password.setValue(password);
    
    ActionResponse result = getRequestLauncher().submitForm(logon, form.form, form.command);
    return result;
  }
  
  
  public void testData() {
    ActionResponse response = tryLogon("Edward", "Shambleshanks");
    assertActionError(response, true);
    
    RenderResponse render = getRequestLauncher().renderView(new SimpleViewParameters(UserDataView.VIEW_ID));
    assertRenderError(render, true);
    
    ActionResponse response2 = tryLogon("Edward", "Longshanks");
    assertActionError(response2, false);
    
    RenderResponse render2 = getRequestLauncher().renderView((ViewParameters) response2.ARIResult.resultingView);
    assertContains(render2, "\"userid\": \"Edward\"");
    
  }
  
  public void testParams() {
    RenderResponse render = getRequestLauncher().renderView(new SimpleViewParameters(ParamsDataView.VIEW_ID));
    assertRenderError(render, false);
    assertContains(render, "\"contextView\": \"\\/templates\\/images\\/image.jpg");
    assertContains(render, "\"selfView\": \"\\/paramdata\"");
  }
  
}
