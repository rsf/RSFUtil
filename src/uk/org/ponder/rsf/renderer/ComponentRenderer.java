/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.view.View;

/** Does the work of rendering a component, given a UIComponent and a location
 * in the template stream. A ComponentRenderer is specific to a particular
 * {@link RenderSystem}, which also contains StaticComponentRenderers for rewriting
 * template data without a corresponding component.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ComponentRenderer {
  /** Returning this value signifies that a leaf tag has been consumed from the
   * template, and that the instruction pointer will be stepped along to 
   * end_close + 1.
   */
  public static final int LEAF_TAG = 1;
  /** Returning this value signifies that a trunk tag has been consumed from 
   * the template, and that the instruction pointer will be stepped along to 
   * end_open + 1.
   */
  public static final int NESTING_TAG = 2;
  /** Renders the supplied component. 
   * @param component The component to be rendered (possibly null)
   * @param view The view in which this component is held
   * @param renderContext A {@link TagRenderContext} object encapulating the template
   * location this component is to be rendered with. The <code>nextpos</code>
   * field in this object will become suitably updated by means of one of the
   * output methods.
   */
  
  public void renderComponent(UIComponent component, View view, 
      TagRenderContext renderContext);    
}
