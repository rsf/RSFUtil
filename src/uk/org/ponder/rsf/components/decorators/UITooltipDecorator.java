/*
 * Created on 26-May-2006
 */
package uk.org.ponder.rsf.components.decorators;

/** Specifies a "Tooltip" which will temporarily display an annotation if 
 * the user focuses on the target component.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class UITooltipDecorator extends UIDecorator {
  public String text;

  public UITooltipDecorator() {}

  public UITooltipDecorator(String text) {
    this.text = text;
  }
}
