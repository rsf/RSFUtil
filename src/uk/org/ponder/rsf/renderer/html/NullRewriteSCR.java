/*
 * Created on 24-May-2006
 */
package uk.org.ponder.rsf.renderer.html;

import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.renderer.StaticComponentRenderer;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.xml.XMLWriter;

/** Default null SCR used on discovering an unrecognised SCR tag.
 * Minimal behaviour of writing the existing open tag, treating it as a 
 * trunk tag.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class NullRewriteSCR implements StaticComponentRenderer {
  
  public static final StaticComponentRenderer instance = new NullRewriteSCR();
  
  public String getName() {
    return null;
  }

  public int render(XMLLump[] lumps, int lumpindex, XMLWriter xmlw) {
    PrintOutputStream pos = xmlw.getInternalWriter();
    XMLLump lump = lumps[lumpindex];
    XMLLump endopen = lump.open_end;
    RenderUtil.dumpTillLump(lumps, lumpindex, endopen.lumpindex + 1, pos);
    return ComponentRenderer.NESTING_TAG;
  }

}
