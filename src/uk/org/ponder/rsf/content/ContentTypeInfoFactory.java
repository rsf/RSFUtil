/*
 * Created on May 6, 2006
 */
package uk.org.ponder.rsf.content;

import java.util.List;
import java.util.Map;

import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * Responsible for determining the ContentTypeInfo and RenderSystem in effect
 * for the current render request. An application-scope bean returning a
 * request-scope result.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class ContentTypeInfoFactory implements ContentTypeReceiver, ContentTypeReporter, ContentTypeResolver {
  private List resolvers;
  private ViewParameters viewparamsproxy;
  private Map typeinfomap;

  private Map viewmap;

  public void setContentTypeResolvers(List resolvers) {
    this.resolvers = resolvers;
  }

  public void setContentTypeInfoMap(Map typeinfomap) {
    this.typeinfomap = typeinfomap;
  }

  public void setViewParameters(ViewParameters viewparamsproxy) {
    this.viewparamsproxy = viewparamsproxy;
  }

  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    viewmap = reflectivecache.getConcurrentMap(1);
  }
  
  public String resolveContentType(ViewParameters viewparams) {
    // For redirect or other "exotic" views, viewID may be null.
    String fromview = viewparams.viewID == null? null : 
      (String) viewmap.get(viewparams.viewID);
    if (fromview == null) {
      for (int i = 0; i < resolvers.size(); ++i) {
        fromview = ((ContentTypeResolver) resolvers.get(i))
            .resolveContentType(viewparams);
        if (fromview != null)
          return fromview;
      }
    }
    return fromview;
  }
  
  public String getContentType() {
    ViewParameters viewparams = (ViewParameters) viewparamsproxy.get();
    return resolveContentType(viewparams);
  }

  public ContentTypeInfo getContentTypeInfo() {
    String contenttype = getContentType();
    ContentTypeInfo togo = (ContentTypeInfo) typeinfomap.get(contenttype);
    if (togo == null) {
      throw UniversalRuntimeException.accumulate(
          new IllegalArgumentException(), "Content type name " + contenttype
              + " has no ContentTypeInfo registered");
    }
    return togo;
  }

  public void setContentType(String viewid, String contenttype) {
    viewmap.put(viewid, contenttype);
  }
}
