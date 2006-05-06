/*
 * Created on May 6, 2006
 */
package uk.org.ponder.rsf.content;

import java.util.Map;

import uk.org.ponder.rsf.renderer.RenderSystem;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.UniversalRuntimeException;

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
    String contenttype = getContentType();
    ContentTypeInfo togo = (ContentTypeInfo) typeinfomap.get(contenttype);
    if (togo == null) {
      throw UniversalRuntimeException.accumulate(new IllegalArgumentException(), 
          "Content type name " + contenttype + " has no ContentTypeInfo registered");
    }
    return togo;
  }
  
  public RenderSystem getRenderSystem() {
    RenderSystem togo = (RenderSystem) systemmap.get(getContentType());
    return (togo == null?  (RenderSystem) systemmap.get(ContentTypeInfoRegistry.DEFAULT) : togo);
  }
}
