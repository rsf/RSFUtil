/*
 * Created on 24 Oct 2006
 */
package uk.org.ponder.rsf.components.decorators;

import uk.org.ponder.rsf.components.UIComponent;

/** Specifies that the decorated component is a label which targets another
 * component. In HTML, this would be represented by a &lt;label for=" tag.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class UILabelTargetDecorator implements UIDecorator {
  public String targetFullID;
  public UILabelTargetDecorator(UIComponent target) {
    targetFullID = target.getFullID();
  }
  
  /** A utility method to register in one step one component as the label
   * for another.
   * @param label The component to be registered as a label
   * @param target The target of the label
   */ 
  public static void targetLabel(UIComponent label, UIComponent target) {
    UIDecorator dec = new UILabelTargetDecorator(target);
    if (label.decorators == null) {
      label.decorators = new DecoratorList(dec);
    }
    else {
      label.decorators.add(dec);
    }
  }
}
