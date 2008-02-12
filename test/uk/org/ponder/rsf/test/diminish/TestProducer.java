/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.diminish;

import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class TestProducer implements ViewComponentProducer {

  public String getViewID() {
    return RequestLauncher.TEST_VIEW;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    UIForm form = UIForm.make(tofill, "form");
    UIInput.make(form, "int1", "diminisher.int1", "0");
    UIInput.make(form, "int2", "diminisher.int2", "0");
    UIInput.make(form, "int3", "diminisher.int3", "0");
  }

}
