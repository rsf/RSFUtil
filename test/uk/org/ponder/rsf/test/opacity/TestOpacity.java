/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.opacity;

import java.util.Arrays;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.rsf.bare.ActionResponse;
import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;


public class TestOpacity extends MultipleRSFTests {
  
  public TestOpacity() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/opacity/opacity-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/opacity/opacity-application-context.xml");
  }
  
  private void testSubmit(Object value, String path) {
    UIForm form = new UIForm();
    form.addParameter(new UIELBinding("opaque." + path, value));
    
    RequestLauncher launcher = getRequestLauncher();

    BeanLocator opaque = (BeanLocator) launcher.getRSACBeanLocator().getBeanLocator().locateBean("opaque");
    Object oldvalue = opaque.locateBean(path);
    
    ActionResponse response = launcher.submitForm(form, null);

    assertNoActionError(response);
    
    Object newvalue = opaque.locateBean(path);
    
    if (oldvalue == null || oldvalue.getClass() == value.getClass()) {
      if (value instanceof String) {
        assertEquals(value, newvalue);
      }
      else {
        assertTrue(Arrays.equals((Object[])value, (Object[])newvalue));
      }
    }
    else {
      if (oldvalue instanceof String[] && value instanceof String) {
        String[] newarray = (String[]) newvalue;
        assertEquals(newarray.length, 1);
        assertEquals(newarray[0], value);
      }
      if (value instanceof String[] && oldvalue instanceof String) {
        String[] valuearray = (String[]) value;
        assertEquals(valuearray, newvalue);
      }
    }
  }
  
  public void testOpacity() {
    String[] values = new String[] {"value1", "value2"};
    
    testSubmit("value", "general");
    testSubmit("value", "string");
    testSubmit("value", "stringarray");

    testSubmit(values, "general");
    // semantics on applying String array to String value are undefined, but will probably
    // end up applying JSON
    //testSubmit(values, "string");
    
    testSubmit(values, "stringarray");

  }
}
