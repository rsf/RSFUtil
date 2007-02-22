/*
 * Created on May 16, 2006
 */
package uk.org.ponder.rsf.components.decorators;

import java.util.Map;

/** Allows free interception of *all* XML attributes in the rendered output.
 * This decorator is virtually as dangerous as {@link uk.org.ponder.rsf.components.UIVerbatim}
 * and the dire warnings of sufferings for users at the head of that class 
 * should be repeated.
 * 
 * The attribute list will be applied <i>on top of</i> attributes inherited
 * from the template, but will be <i>overwritten by</i> any component-specific
 * attributes applied by the renderer for the component to which this decorator
 * is attached.
 * 
 * Please try to think of technology-neutral abstractions rather than using this
 * class, wherever possible.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIFreeAttributeDecorator implements UIDecorator {
  /** A map of String attribute names to String attribute values which will
   * be used to override any attributes taken from the template.
   */
  public Map attributes;

  public UIFreeAttributeDecorator() {
  }

  public UIFreeAttributeDecorator(Map attributes) {
    this.attributes = attributes;
  }
  
}
