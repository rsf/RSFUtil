/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.xml.XMLWriter;

/** A component renderer that is dispatched by its name, with no peer component */
public interface StaticComponentRenderer {
 
  public String getName();
  public int render(XMLLump[] lumps, int lumpindex, XMLWriter xmlw);
}
