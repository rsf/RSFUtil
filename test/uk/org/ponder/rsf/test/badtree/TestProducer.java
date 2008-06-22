/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.badtree;

import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class TestProducer implements ViewComponentProducer, DefaultView {

  public String getViewID() {
    return RequestLauncher.TEST_VIEW;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
   
    UIBranchContainer branch1 = UIBranchContainer.make(tofill, "branch1:");
    UIBranchContainer branch2 = UIBranchContainer.make(branch1, "branch2:");
    branch2.addComponent(branch1);
  }

}
