/*
 * Created on 24-May-2006
 */
package uk.org.ponder.rsf.renderer.scr;

import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.xml.XMLWriter;

/** Default null SCR used on discovering an unrecognised SCR tag.
 * Minimal behaviour of writing the existing open tag, treating it as a 
 * trunk tag.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class NullRewriteSCR implements BasicSCR {
  
  public static final BasicSCR instance = new NullRewriteSCR();
  
  public String getName() {
    return null;
  }

  public int render(XMLLump lump, XMLWriter xmlw) {
    PrintOutputStream pos = xmlw.getInternalWriter();
    XMLLump endopen = lump.open_end;
    RenderUtil.dumpTillLump(lump.parent.lumps, lump.lumpindex, 
        endopen.lumpindex + 1, pos);
    return ComponentRenderer.NESTING_TAG;
  }

}
