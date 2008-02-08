/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.converter;

import uk.org.ponder.rsf.bare.ActionResponse;
import uk.org.ponder.rsf.bare.RenderResponse;
import uk.org.ponder.rsf.bare.ViewWrapper;
import uk.org.ponder.rsf.bare.junit.PlainRSFTests;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;

// Test for composite Resulting View Bindings issue, reported in forums at
// http://ponder.org.uk/rsf/posts/list/8.page

public class TestConverter extends PlainRSFTests {
  
  public TestConverter() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/converter/converter-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/converter/converter-application-context.xml");
  }
  
  public boolean isSingleShot() {
    return false;
  }
  
  public void testConverter() {
    RenderResponse response = getRequestLauncher().renderView();
    ViewWrapper wrapper = response.viewWrapper;
    
    UIForm form = (UIForm) wrapper.queryComponent(new UIForm());
    UICommand command = (UICommand) wrapper.queryComponent(new UICommand());
    
    DateHolder defaultHolder = new DateHolder();
    
    ActionResponse result = getRequestLauncher().submitForm(form, command);
    DateHolder dateHolder = (DateHolder) result.requestContext.locateBean("dateHolder");
    
    assertEquals(dateHolder.date, defaultHolder.date);
    
    String testDate = "2005-05-03"; 
    
    UIInput input = (UIInput) wrapper.queryComponent(new UIInput());
    input.setValue(testDate);
    
    ActionResponse result2 = getRequestLauncher().submitForm(form, command);
    DateHolder dateHolder2 = (DateHolder) result2.requestContext.locateBean("dateHolder");
  
    DateConverter converter = new DateConverter();
    assertEquals(dateHolder2.date, converter.parse(testDate));
    
  }
}
