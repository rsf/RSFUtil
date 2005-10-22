/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.xml.XMLWriter;

/** Does the work of rendering a component, given a UIComponent and a location
 * in the template stream. A ComponentRenderer is specific to a particular
 * @see RenderSystem, which also contains StaticComponentRenderers for rewriting
 * template data without a corresponding component.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ComponentRenderer {
  /** Returning this value signifies that a leaf tag has been consumed from the
   * template, and that the instruction pointer will be stepped along to 
   * end_close + 1.
   */
  public static final int LEAF_TAG = 1;
  /** Returning this value signifies that a trunk tag has been consumed (slightly
   * rare - e.g. &lt;form&gt; or &lt;body&gt;) from the template, and that
   * the instruction pointer will be stepped along to end_open + 1.
   */
  public static final int NESTING_TAG = 2;
  /** @param component The component to be rendered (possibly null)
   * @param lumps The array of XML lumps constituting the condensed template
   * representation.
   * @return An index chosen from one of the two values above, indicating the
   * new value of the instruction pointer in the template, after this render
   * is concluded.
   */
  
  public int render(UIComponent component, XMLLump[] lumps, int lumpindex, 
      XMLWriter xmlw);    
}
