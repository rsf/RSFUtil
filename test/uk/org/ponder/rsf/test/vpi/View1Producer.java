/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.vpi;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class View1Producer implements ViewComponentProducer {
  public static final String VIEW_ID = "view1";
  
  public String getViewID() {
    return VIEW_ID;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
 
  }
}
