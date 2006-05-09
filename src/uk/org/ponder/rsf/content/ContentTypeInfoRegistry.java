/*
 * Created on May 6, 2006
 */
package uk.org.ponder.rsf.content;

import java.util.HashMap;
import java.util.Map;

/** Holds a registry of core ContentTypeInfo records.
 * To implement a user-defined set, simply provide your own definition
 * for the RSF application-scope bean named "contentTypeInfoMap".
 * **/

public class ContentTypeInfoRegistry {
  public static final String HTML = "HTML";
  public static final String AJAX = "AJAX";
  public static final String XUL = "XUL";
  public static final String SVG = "SVG";
  public static final String RSS = "RSS";
  
  public static final String DEFAULT = "DEFAULT";
  /** A default ContentTypeInfo entry for HTML content */
  
  public static final ContentTypeInfo HTML_CONTENTINFO = 
    new ContentTypeInfo(HTML, "html",  "<!DOCTYPE html      "
        + "PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
        + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
        "text/html; charset=UTF-8"
        );
  
  public static final ContentTypeInfo AJAX_CONTENTINFO = 
    new ContentTypeInfo(AJAX, "xml",  "",
        "application/xml; charset=UTF-8"
        );

  public static final ContentTypeInfo XUL_CONTENTINFO = 
    new ContentTypeInfo(XUL, "xul",  "",
        "application/vnd.mozilla.xul+xml; charset=UTF-8"
        );
  
  private static Map contentmap = new HashMap();
  
  public static void addContentTypeInfo(Map map, ContentTypeInfo toadd) {
    map.put(toadd.typename, toadd);
  }
  
  static {
    addContentTypeInfo(contentmap, HTML_CONTENTINFO);
    addContentTypeInfo(contentmap, AJAX_CONTENTINFO);
    addContentTypeInfo(contentmap, XUL_CONTENTINFO);
  }
  
  public static Map getContentTypeInfoMap() {
    return contentmap;
  }
}
