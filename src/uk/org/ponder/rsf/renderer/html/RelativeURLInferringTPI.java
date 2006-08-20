/*
 * Created on 19 Aug 2006
 */
package uk.org.ponder.rsf.renderer.html;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.htmlutil.HTMLConstants;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.template.ContentTypedTPI;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.stringutil.URLUtil;

/** This TemplateParseInterceptor automatically marks up all relative URLs
 * on relevant HTML tags with the rsf:id="scr=rewrite-url" attribute.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class RelativeURLInferringTPI implements ContentTypedTPI {

  public static Map tagToAttrName = new HashMap();
  
  static {
    for (int i = 0; i < HTMLConstants.tagtoURL.length; ++i) {
      String[] tags = HTMLConstants.tagtoURL[i];
      for (int j = 1; j < tags.length; ++j) {
        tagToAttrName.put(XMLLump.textToTag(tags[j]), tags[0]);
      }
    }
  }
  
  public void adjustAttributes(String tag, Map attributes) {
    String attrname = (String) tagToAttrName.get(tag);
    if (attrname == null) return;
    if (attributes.get(XMLLump.ID_ATTRIBUTE) != null) return;
    String url = (String) attributes.get(attrname);
    if (url == null || URLUtil.isAbsolute(url) || url.charAt(0) == '/') return;
    attributes.put(XMLLump.ID_ATTRIBUTE, XMLLump.SCR_PREFIX + URLRewriteSCR.NAME);
  }

  public String[] getInterceptedContentTypes() {
    return new String[] {
        ContentTypeInfoRegistry.HTML, ContentTypeInfoRegistry.HTML_FRAGMENT
    };
  }

}
