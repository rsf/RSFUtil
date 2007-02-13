/*
 * Created on 18 Sep 2006
 */
package uk.org.ponder.rsf.renderer.html;

import java.util.HashSet;
import java.util.Set;

import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.renderer.scr.BasicSCR;
import uk.org.ponder.rsf.renderer.scr.CollectingSCR;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.template.XMLLumpList;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.xml.XMLWriter;

/** A basic collector of &lt;head&gt; material for HTML pages. Will
 * emit all collected &lt;style&gt; and &lt;script&gt; tags, and leave the
 * tag in an open condition.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */ 

public class HeadCollectingSCR implements CollectingSCR {
  public static final String NAME = "head-collect";
  private BasicSCR urlRewriteSCR;
  public String getName() {
    return NAME;
  }

  public String[] getCollectingNames() {
    return new String[] {"style", "script"};
  }

  private static String[] uniquifyAttrs = new String[] {"href", "src"}; 
  private static String getUniquingAttr(XMLLump lump) {
    String val = null;
    for (int i = 0; i < uniquifyAttrs.length; ++ i) {
      String thisval = (String) lump.attributemap.get(uniquifyAttrs[i]);
      if (thisval != null) {
        val = thisval; break;
        }
      }
    return val;
  }
  
  public void setURLRewriteSCR(BasicSCR urlRewriteSCR) {
    this.urlRewriteSCR = urlRewriteSCR;
  }

  public int render(XMLLump lump, XMLLumpList collected, XMLWriter xmlw) {
    PrintOutputStream pos = xmlw.getInternalWriter();
    RenderUtil.dumpTillLump(lump.parent.lumps, lump.lumpindex, 
        lump.open_end.lumpindex + 1, pos);
    Set used = new HashSet();
    for (int i = 0; i < collected.size(); ++ i) {
      XMLLump collump = collected.lumpAt(i);
      String attr = getUniquingAttr(collump);
      if (attr != null) {
        if (used.contains(attr)) continue;
        else used.add(attr);
      }
      urlRewriteSCR.render(collump, xmlw);
      RenderUtil.dumpTillLump(collump.parent.lumps, collump.open_end.lumpindex + 1, 
          collump.close_tag.lumpindex + 1, pos);
    }
    return ComponentRenderer.NESTING_TAG; 
  }

}
