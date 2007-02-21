/*
 * Created on 26-May-2006
 */
package uk.org.ponder.rsf.components.decorators;

import uk.org.ponder.rsf.components.UIBoundString;

/** Specifies a "Tooltip" which will temporarily display an annotation if 
 * the user focuses on the target component.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class UITooltipDecorator extends UIBoundStringDecorator {
  public UITooltipDecorator() {}

  public UITooltipDecorator(String text) {
    this.text = new UIBoundString(text);
  }
  
  public UITooltipDecorator(UIBoundString text) {
    this.text = text;
  }
}
