/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.converter;

import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIDeletionBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class TestProducer2 implements ViewComponentProducer {
  
  public static final String VIEW_ID = "test2";
  
  public String getViewID() {
    return VIEW_ID;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    UIForm dateform = UIForm.make(tofill, "converter-form");
    dateform.addParameter(new UIDeletionBinding("profileOtherWorksBL.4", 
         new ELReference("removeIds")));
    UICommand.make(dateform, "date-submit");
  }

}
