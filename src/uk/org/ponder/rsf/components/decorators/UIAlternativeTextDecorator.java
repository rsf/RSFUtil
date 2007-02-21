/*
 * Created on 20 Feb 2007
 */
package uk.org.ponder.rsf.components.decorators;

import uk.org.ponder.rsf.components.UIBoundString;

/** Alternative text to be displayed when a user agent cannot or chooses 
 * not to display an element.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIAlternativeTextDecorator extends UIBoundStringDecorator {
  public UIAlternativeTextDecorator() {}

  public UIAlternativeTextDecorator(String text) {
    this.text = new UIBoundString(text);
  }
  
  public UIAlternativeTextDecorator(UIBoundString text) {
    this.text = text;
  }
}
