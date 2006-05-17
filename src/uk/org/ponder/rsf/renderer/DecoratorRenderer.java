/*
 * Created on May 16, 2006
 */
package uk.org.ponder.rsf.renderer;

import java.util.Map;

import uk.org.ponder.rsf.components.decorators.UIDecorator;

/** The context for a decorator consists of a map of incoming attributes from
 * the template.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface DecoratorRenderer {
  /** The class of Decorator that this renderer renders */
  public Class getRenderedType();
  /** A comma-separated list of content type names that this renderer is good for
   * (as listed in {@link uk.org.ponder.rsf.content.ContentTypeInfoRegistry})
   */
  public String getContentTypes();
  /** Perform the rendering operation by modifying the supplied attribute map
   * (a map of String to String).
   */
  public void modifyAttributes(UIDecorator decorator, String tagname, Map tomodify);
}
