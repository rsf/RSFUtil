/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.badparams;

import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.viewstate.support.ViewParamsValidator;

/** Test for various "bad viewparams" issues RSF-50 and RSF-23
 */

public class TestBadParams extends MultipleRSFTests {
  
  public TestBadParams() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/badparams/badparams-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/badparams/badparams-application-context.xml");
  }
  
  public void testBadParams() {
    
    ViewParamsValidator validator = (ViewParamsValidator) applicationContext.getBean("viewParamsValidator");
    
    assertEquals(2, validator.errors.size());
    // Test http://www.caret.cam.ac.uk/jira/browse/RSF-50
    assertNotNull(validator.errors.get(BadParams1.class));
    // Test http://www.caret.cam.ac.uk/jira/browse/RSF-23 as well as testing ViewParamsRegistrar
    assertNotNull(validator.errors.get(BadParams2.class));
    
  }
  
}
