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
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/** Test for DataConverter issue RSF-47, reported in forums at
 *http://ponder.org.uk/rsf/posts/list/183.page
 *
 *<br/>This test case also demonstrates more advanced usage of RSF "full-cycle"
 * tests. It performs 3 full request cycles, firstly one render cycle and then
 * two action cycles, and also demonstrates use of ComponentQueries, modification
 * and submission of a pure component tree.
 */

public class TestConverter extends PlainRSFTests {
  
  public TestConverter() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/converter/converter-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/converter/converter-application-context.xml");
  }
  
  public boolean isSingleShot() {
    return false;
  }
  
  public void yestConverterInapplicable() {
    // Fire an initial request to render the view from TestProducer.java
    RenderResponse response = getRequestLauncher().renderView();
    ViewWrapper wrapper = response.viewWrapper;
    
    UIForm form = (UIForm) wrapper.queryComponent(new UIForm());
    UICommand command = (UICommand) wrapper.queryComponent(new UICommand());
    
    DateHolder defaultHolder = new DateHolder();
    
    // Fire an action request to submit the form with no changes. The UIInput
    // will be submitted with no changes, thus forming an "Inapplicable Value".
    ActionResponse result = getRequestLauncher().submitForm(form, command);
    DateHolder dateHolder = (DateHolder) result.requestContext.locateBean("dateHolder");
    
    assertEquals(dateHolder.date, defaultHolder.date);
    
    // Now test the correct operation of the DataConverter (DateConverter) in
    // parsing this simulated user input into the required Date object, in a 3rd
    // request.
    String testDate = "2005-05-03"; 
    
    UIInput input = (UIInput) wrapper.queryComponent(new UIInput());
    input.setValue(testDate);
    
    ActionResponse result2 = getRequestLauncher().submitForm(form, command);
    DateHolder dateHolder2 = (DateHolder) result2.requestContext.locateBean("dateHolder");
  
    DateConverter converter = new DateConverter();
    assertEquals(dateHolder2.date, converter.parse(testDate));
  }
  
  /** Test for RSF-57. The condition is that the form should submit correctly without
   * an attempt to trigger the "ExplosiveConverter".
   */
  public void testConverterPathMatch() {
    RenderResponse response = getRequestLauncher().renderView(new SimpleViewParameters(TestProducer2.VIEW_ID));
    ViewWrapper wrapper = response.viewWrapper;
    
    UIForm form = (UIForm) wrapper.queryComponent(new UIForm());
    getRequestLauncher().submitForm(form, null);
  }
}
