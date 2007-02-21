/*
 * Created on May 16, 2006
 */
package uk.org.ponder.rsf.components.decorators;

import java.awt.Color;

/** Alters the foreground and background rendering colours of the decorated
 * component. Unless these are "computed" colours (i.e. those not drawn from
 * a fixed subset), it would be preferable to define these in template-side
 * style sheets or other client-side definitions.
 * </p>A <code>null</code> value for either <code>foreground</code> or
 * <code>background</code> indicates that any existing client definition for
 * the colour will be left unchanged.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIColourDecorator implements UIDecorator {
  public Color foreground;
  public Color background;
  
  public UIColourDecorator(Color foreground, Color background) {
    this.foreground = foreground;
    this.background = background;
  }
  
  public UIColourDecorator() {
  }
}
