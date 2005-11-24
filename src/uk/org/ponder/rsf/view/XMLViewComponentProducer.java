/*
 * Created on Nov 24, 2005
 */
package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class XMLViewComponentProducer implements ViewComponentProducer {
  private String viewID;
  private UIContainer container;
  public void setViewID(String viewID) {
    this.viewID = viewID;
  }
  public String getViewID() {
    return viewID;
  }

  public void setContainer(UIContainer container) {
    this.container = container;
  }
  
  public void fillComponents(UIContainer tofill, 
      ViewParameters origviewparams, ComponentChecker checker) {
    // TODO Auto-generated method stub
    
  }

}
