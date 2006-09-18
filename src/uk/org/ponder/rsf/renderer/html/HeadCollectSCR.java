/*
 * Created on 18-Sep-2006
 */
package uk.org.ponder.rsf.renderer.html;

import uk.org.ponder.rsf.renderer.CollectingSCR;
import uk.org.ponder.rsf.renderer.StaticComponentRenderer;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.xml.XMLWriter;

/** Annotates a piece of "head matter" in a multi-file template set that
 * needs to be "collected" into the HTML &lt;head&gt; of the final
 * rendered product.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class HeadCollectSCR implements StaticComponentRenderer, CollectingSCR {
  public static final String COLLECT_NAME = "head";

  public String getName() {
    return COLLECT_NAME;
  }

  public int render(XMLLump lump, XMLWriter xmlw) {
    return NullRewriteSCR.instance.render(lump, xmlw);
  }
}
