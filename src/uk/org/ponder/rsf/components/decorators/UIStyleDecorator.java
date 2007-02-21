/*
 * Created on May 16, 2006
 */
package uk.org.ponder.rsf.components.decorators;

/** A decorator which registers a String value representing a "style class"
 * for a component. In HTML, will become the "class" attribute.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIStyleDecorator implements UIDecorator {
  public String styleclass;

  public UIStyleDecorator(String styleclass) {
    this.styleclass = styleclass;
  }

  public UIStyleDecorator() {
  }
}
