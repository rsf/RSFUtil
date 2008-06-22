/*
 * Created on 22 Jun 2008
 */
package uk.org.ponder.rsf.test.badtree;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIOutput;
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
    UIBranchContainer joint = UIBranchContainer.make(tofill, "joint:");
    UIOutput.make(joint, "leaf");
    UIOutput.make(joint, "leaf");
    
  }

}
