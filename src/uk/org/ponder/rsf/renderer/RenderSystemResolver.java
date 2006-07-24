/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.renderer;

import java.util.Map;

import uk.org.ponder.rsf.content.ContentTypeInfo;

/** Resolves the RenderSystem to be in force for the current request. 
 * Actually an application scope bean, but acquires ContentTypeInfo via proxy. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class RenderSystemResolver {

  public static final String DEFAULT = "DEFAULT";

  private Map systemmap;

  private ContentTypeInfo contenttypeinfo;

  public void setRenderSystemMap(Map systemmap) {
    this.systemmap = systemmap;
  }

  public void setContentTypeInfo(ContentTypeInfo contenttypeinfo) {
    this.contenttypeinfo = contenttypeinfo;
  }
  
  public RenderSystem getRenderSystem() {
    RenderSystem togo = (RenderSystem) systemmap.get(contenttypeinfo.get().typename);
    return (togo == null?  (RenderSystem) systemmap.get(DEFAULT) : togo);
  }
  
}
