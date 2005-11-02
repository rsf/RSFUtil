/*
 * Created on Nov 2, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.rsf.componentprocessor.ComponentProcessor;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class BasicFormFixer implements ComponentProcessor {
  private ViewParameters viewparams;

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }
  
  public int processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIForm) {
      UIForm toprocess = (UIForm) toprocesso; 
    }
  }

}
