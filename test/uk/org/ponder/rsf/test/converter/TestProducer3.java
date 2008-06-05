/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.converter;

import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class TestProducer3 implements ViewComponentProducer {
  
  public static final String VIEW_ID = "test3";
  
  public String getViewID() {
    return VIEW_ID;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    UIForm form = UIForm.make(tofill, "converter-form");
    UIBoundBoolean bool = UIBoundBoolean.make(form, "input", "stringHolder.string", true);
    bool.mustapply = true;
  }

}
