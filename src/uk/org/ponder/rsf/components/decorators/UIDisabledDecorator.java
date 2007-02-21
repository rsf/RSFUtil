/*
 * Created on 9 Feb 2007
 */
package uk.org.ponder.rsf.components.decorators;

/**
 * A decorator which allows a control to be marked as "disabled" in the interface.
 * Disabled controls can not accept user input and will typically be rendered
 * differently. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIDisabledDecorator implements UIDecorator {
  public boolean disabled = true;
  public UIDisabledDecorator() {}
  public UIDisabledDecorator(boolean disabled) {
    this.disabled = disabled;
  }
}
