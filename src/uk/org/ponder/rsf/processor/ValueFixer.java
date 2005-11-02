/*
 * Created on Nov 1, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.rsf.componentprocessor.ComponentProcessor;
import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIComponent;

public class ValueFixer implements ComponentProcessor{

  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIBound) {
      UIBound toprocess = (UIBound) toprocesso;
      
    }
  }

}
