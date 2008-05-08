/*
 * Created on 18-Sep-2006
 */
package uk.org.ponder.rsf.renderer.html;

import uk.org.ponder.rsf.renderer.TagRenderContext;
import uk.org.ponder.rsf.renderer.scr.NullRewriteSCR;
import uk.org.ponder.rsf.renderer.scr.StaticComponentRenderer;
import uk.org.ponder.rsf.template.XMLLump;

/** Annotates a piece of "head matter" in a multi-file template set that
 * needs to be "collected" into the HTML &lt;head&gt; of the final
 * rendered product. This renderer is never used concretely, it is simply
 * a tag recognised by the template parser, which collects scr names beginning
 * with the prefix {@link XMLLump#SCR_CONTRIBUTE_PREFIX}.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class HeadContributingSCR implements StaticComponentRenderer {
  public static final String CONTRIBUTE_NAME = "head";

  public String getName() {
    return CONTRIBUTE_NAME;
  }

  public int render(TagRenderContext trc) {
    return NullRewriteSCR.instance.render(trc);
  }
}
