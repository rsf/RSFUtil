/*
 * Created on 6 Oct 2006
 */
package uk.org.ponder.rsf.template;

import java.util.Map;

import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;

public class RSFNamespaceRemovingTPI implements ContentTypedTPI {
  public void adjustAttributes(String tag, Map attributes) {
    if (attributes.containsKey("xmlns:rsf")) {
      attributes.remove("xmlns:rsf");
    }
  }

  public String[] getInterceptedContentTypes() {
    return new String[] { ContentTypeInfoRegistry.HTML,
        ContentTypeInfoRegistry.HTML_FRAGMENT };
  }
}
