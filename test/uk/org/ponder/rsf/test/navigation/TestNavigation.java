/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.navigation;

import uk.org.ponder.rsf.bare.ActionResponse;
import uk.org.ponder.rsf.bare.RenderResponse;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.util.Constants;

/** Test for ID NPE RSF-71
 */


public class TestNavigation extends MultipleRSFTests {
  
  public TestNavigation() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/navigation/navigation-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/navigation/navigation-application-context.xml");
  }
   
  public void testRender() {
    assertEquals("default", submitForm("nullMethod"));
    assertEquals("first", submitForm("stringMethod.'first'"));
    assertEquals("default", submitForm("stringMethod.'" + Constants.NULL_STRING + "'"));
    assertEquals("default", submitForm("stringMethod.'third'"));
    assertEquals("third", submitForm("stringMethod.'second'"));
  }

  private String submitForm(String el) {
    RenderResponse render = getRequestLauncher().renderView(new ELParams("actionBean." + el));
    UIForm form = (UIForm) render.viewWrapper.queryComponent(new UIForm());
    UICommand command = (UICommand) render.viewWrapper.queryComponent(new UICommand());
    ActionResponse response = getRequestLauncher().submitForm(form, command);
    assertActionError(response, false);
    SimpleViewParameters resulting = (SimpleViewParameters) response.ARIResult.resultingView;
    return resulting.viewID;
  }
}
