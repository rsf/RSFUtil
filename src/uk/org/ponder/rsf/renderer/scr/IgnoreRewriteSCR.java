/*
 * Created on 24-May-2006
 */
package uk.org.ponder.rsf.renderer.scr;

import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.TagRenderContext;

/** Ignore SCR tag, which will cause the target tag and all children to
 * be removed from rendered output.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class IgnoreRewriteSCR implements BasicSCR {
  
  public static final BasicSCR instance = new IgnoreRewriteSCR();
  
  public String getName() {
    return "ignore";
  }

  public int render(TagRenderContext trc) {
    return ComponentRenderer.LEAF_TAG;
  }

}
