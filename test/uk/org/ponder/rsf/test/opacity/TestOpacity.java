/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.opacity;

import java.util.Arrays;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.bare.ActionResponse;
import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.bare.junit.MultipleRSFTests;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInputMany;

/** Test for numerous data conversion cases, where the target type is both known and 
 * unknocn, incoming from both pure bindings and components
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class TestOpacity extends MultipleRSFTests {

  public TestOpacity() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsf/test/opacity/opacity-request-context.xml");
    contributeConfigLocation("classpath:uk/org/ponder/rsf/test/opacity/opacity-application-context.xml");
  }

  private void testSubmit(Object value, String path) {
    testSubmit(value, path, false, false);
    testSubmit(value, path, false, true);
  }

  private TargettedMessageList testSubmit(Object value, String path, boolean fail, boolean component) {
    UIForm form = new UIForm();
    String fullpath = "opaque." + path;
    if (component) {
      if (value instanceof String) {
        UIInput input = UIInput.make(form, "input", fullpath, (String) value);
        input.mustapply = true;
      }
      else if (value instanceof String[]) {
        UIInputMany input = UIInputMany.make(form, "input", fullpath, (String[]) value);
        input.mustapply = true;
      }
    }
    else {
      form.addParameter(new UIELBinding(fullpath, value));
    }

    RequestLauncher launcher = getRequestLauncher();

    BeanLocator opaque = (BeanLocator) launcher.getRSACBeanLocator().getBeanLocator()
        .locateBean("opaque");
    Object oldvalue = opaque.locateBean(path);

    ActionResponse response = launcher.submitForm(form, null);

    assertActionError(response, fail);

    if (fail)
      return fetchMessages(response);

    Object newvalue = opaque.locateBean(path);

    if (oldvalue == null || oldvalue.getClass() == value.getClass()) {
      if (value instanceof String) {
        assertEquals(value, newvalue);
      }
      else {
        assertTrue(Arrays.equals((Object[]) value, (Object[]) newvalue));
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
    return null;
  }

  public void testOpacity() {
    String[] values = new String[] { "value1", "value2" };
    testSubmit(values, "general");
    

    String wrongEL = "el.expression";
    TargettedMessageList list = testSubmit(wrongEL, "inconvertible.producer", true, false);
    assertEquals(1, list.size());
    TargettedMessage message = list.messageAt(0);
    assertTrue(message.exception.getMessage().indexOf(wrongEL) != -1);

    testSubmit("el.expression", "inconvertible");

    testSubmit("value", "general");
    testSubmit("value", "string");
    testSubmit("value", "stringarray");


    // semantics on applying String array to String value are undefined, but will probably
    // end up applying JSON
    // testSubmit(values, "string");

    testSubmit(values, "stringarray");

  }
}
