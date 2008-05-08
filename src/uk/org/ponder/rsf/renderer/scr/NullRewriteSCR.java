/*
 * Created on 24-May-2006
 */
package uk.org.ponder.rsf.renderer.scr;

import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.TagRenderContext;

/** Default null SCR used on discovering an unrecognised SCR tag.
 * Minimal behaviour of writing the existing open tag, treating it as a 
 * trunk tag.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class NullRewriteSCR implements BasicSCR {
  
  public static final BasicSCR instance = new NullRewriteSCR();
  
  public String getName() {
    return "null";
  }

  public int render(TagRenderContext trc) {
    trc.openTag();
    trc.replaceAttributesOpen();
    return ComponentRenderer.NESTING_TAG;
  }

}
