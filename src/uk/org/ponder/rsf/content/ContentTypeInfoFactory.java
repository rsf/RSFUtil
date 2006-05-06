/*
 * Created on May 6, 2006
 */
package uk.org.ponder.rsf.content;

import java.util.Map;

import uk.org.ponder.rsf.renderer.RenderSystem;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Responsible for determining the ContentTypeInfo and RenderSystem in effect
 * for the current render request.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ContentTypeInfoFactory {
  private ContentTypeResolver resolver;
  private Map systemmap;
  private ViewParameters viewparams;
  private Map typeinfomap;

  public void setContentTypeResolver(ContentTypeResolver resolver) {
    this.resolver = resolver;
  }
  
  public void setRenderSystemMap(Map systemmap) {
    this.systemmap = systemmap;
  }
  
  public void setContentTypeInfoMap(Map typeinfomap) {
    this.typeinfomap = typeinfomap;
  }
  
  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }
  
  public String getContentType() {
    return resolver.resolveContentType(viewparams);
  }
  
  public ContentTypeInfo getContentTypeInfo() {
    return (ContentTypeInfo) typeinfomap.get(getContentType());
  }
  
  public RenderSystem getRenderSystem() {
    RenderSystem togo = (RenderSystem) systemmap.get(getContentType());
    return (togo == null?  (RenderSystem) systemmap.get(ContentTypeInfoRegistry.DEFAULT) : togo);
  }
}
