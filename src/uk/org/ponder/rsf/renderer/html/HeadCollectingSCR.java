/*
 * Created on 18 Sep 2006
 */
package uk.org.ponder.rsf.renderer.html;

import uk.org.ponder.rsf.renderer.scr.CollectingSCR;
import uk.org.ponder.rsf.renderer.scr.StaticComponentRenderer;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.xml.XMLWriter;

public class HeadCollectingSCR implements StaticComponentRenderer, CollectingSCR {
  public static final String NAME = "head-collect";
  public String getName() {
    return NAME;
  }

  public int render(XMLLump lump, XMLWriter xmlw) {
    // TODO Auto-generated method stub
    return 0;
  }

  public String getCollectingNames() {
    // TODO Auto-generated method stub
    return null;
  }

}
