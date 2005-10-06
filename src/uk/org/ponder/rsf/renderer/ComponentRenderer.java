/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.xml.XMLWriter;

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
   * @return An index as specified above, indicating the value of the resulting
   * instruction pointer.
   */
  
  public int render(UIComponent component, XMLLump[] lumps, int lumpindex, 
      XMLWriter xmlw);    
}
