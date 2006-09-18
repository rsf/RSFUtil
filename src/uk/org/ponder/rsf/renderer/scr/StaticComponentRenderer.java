/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer.scr;

import uk.org.ponder.rsf.renderer.ComponentRenderer;

/** A component renderer that is dispatched by its name, with no peer component 
 * @see ComponentRenderer*/
public interface StaticComponentRenderer {
  /** The name used to index this renderer in its parent {@link StaticRendererCollection},
   * as well as to refer to it from a template file using the attribute 
   * <code>rsf:id="scr=name"</code>
   */
  public String getName();
 
}
