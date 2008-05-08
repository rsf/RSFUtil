/*
 * Created on 18 Sep 2006
 */
package uk.org.ponder.rsf.renderer.scr;

import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.TagRenderContext;

public interface BasicSCR extends StaticComponentRenderer {
  /** Renders the rewritten template data to the output stream.
   * @param trc A TagRenderContext specifying the rewrite rule to be operated
   * @return An integer code identifying the action this renderer took, either
   * {@link ComponentRenderer#LEAF_TAG} or {@link ComponentRenderer#NESTING_TAG}
   * depending on whether just the opening tag or also the closing tag was consumed from 
   * the template. 
   */
  public int render(TagRenderContext trc);
}
