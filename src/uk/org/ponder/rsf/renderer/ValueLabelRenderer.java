/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.renderer;
/** This interface is used to render the "labels" displayed for selection
 * controls (e.g. in HTML, drop-downs and multiple selections). 
 * The DefaultLabelRenderer simply calls toString() on the supplied value. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ValueLabelRenderer {
  public String renderLabel(Object value);
}
