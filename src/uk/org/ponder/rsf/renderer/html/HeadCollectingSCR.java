/*
 * Created on 18 Sep 2006
 */
package uk.org.ponder.rsf.renderer.html;

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

  public void setURLRewriteSCR(BasicSCR urlRewriteSCR) {
    this.urlRewriteSCR = urlRewriteSCR;
  }

  public int render(XMLLump lump, XMLLumpList collected, XMLWriter xmlw) {
    PrintOutputStream pos = xmlw.getInternalWriter();
    RenderUtil.dumpTillLump(lump.parent.lumps, lump.lumpindex, 
        lump.open_end.lumpindex + 1, pos);
    for (int i = 0; i < collected.size(); ++ i) {
      XMLLump collump = collected.lumpAt(i);
      urlRewriteSCR.render(collump, xmlw);
      RenderUtil.dumpTillLump(collump.parent.lumps, collump.open_end.lumpindex + 1, 
          collump.close_tag.lumpindex + 1, pos);
    }
    return ComponentRenderer.NESTING_TAG; 
  }

}
