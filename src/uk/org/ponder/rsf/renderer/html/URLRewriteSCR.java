/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer.html;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.htmlutil.HTMLConstants;
import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.renderer.scr.BasicSCR;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.view.BasedViewTemplate;
import uk.org.ponder.rsf.viewstate.ContextURLProvider;
import uk.org.ponder.rsf.viewstate.URLRewriter;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.xml.XMLUtil;
import uk.org.ponder.xml.XMLWriter;

/** Performs a URL-rewrite for a template-relative
 * "resource" URL (e.g. image or CSS) as found in a template.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class URLRewriteSCR implements BasicSCR {
  public static final String NAME = "rewrite-url";
  private URLRewriter rewriter;
  private ContextURLProvider cup;
//  private String resourcebase;

  public String getName() {
    return NAME;
  }

  public void setURLRewriter(URLRewriter resolver) {
    this.rewriter = resolver;
  }

  public void setContextBaseProvider(ContextURLProvider cup) {
    this.cup = cup;
  }
  
  public String resolveURL(BasedViewTemplate template, String toresolve) {
    String extresourcebase = template.getExtResourceBase();
    if (extresourcebase == null || extresourcebase == "") {
      extresourcebase = cup.getContextBaseURL();
    }
    String resourcebase = extresourcebase + template.getRelativeResourceBase();
    return rewriter.rewriteResourceURL(toresolve, resourcebase);
  }

  /**
   * @param attrs
   *          A non-modifiable map of the attributes that may need to be
   *          filtered.
   * @param name
   *          The name of an attribute whose value is a URL requiring rewriting.
   * @param cloned
   *          Either a modifiable map of the attributes, or <code>null</code>
   *          if none yet exists.
   * @return Either <code>null</code> if no modification was performed, or a
   *         modifiable attribute map.
   */
  public Map getResolvedURLMap(XMLLump lump, String name, Map cloned) {
    String toresolve = (String) lump.attributemap.get(name);
    if (toresolve == null || rewriter == null) return cloned;
    String resolved = resolveURL(lump.parent, toresolve);
    if (resolved == null) {
      return cloned;
    }
    else {
      Map togo = null;
      if (cloned == null) {
        togo = new HashMap();
        togo.putAll(lump.attributemap);
      }
      else {
        togo = cloned;
      }
      togo.put(name, resolved);
      return togo;
    }
  }

  public static String getLinkAttribute(XMLLump lump) {
    for (int i = 0; i < HTMLConstants.tagtoURL.length; ++i) {
      String[] tags = HTMLConstants.tagtoURL[i];
      String tag = tags[0];
      for (int j = 1; j < tags.length; ++j) {
        if (lump.textEquals(tags[j]))
          return tag;
      }
    }
    return null;
  }
 
  public Map rewriteURLs(XMLLump lump, Map attrcopy) {
    String linkattr = getLinkAttribute(lump);
    attrcopy = getResolvedURLMap(lump, linkattr, attrcopy);
    for (int i = 0; i < HTMLConstants.ubiquitousURL.length; ++i) {
      attrcopy = getResolvedURLMap(lump, HTMLConstants.ubiquitousURL[i], attrcopy);
    }
    return attrcopy;
  }
  
  public int render(XMLLump lump, XMLWriter xmlw) {
    PrintOutputStream pos = xmlw.getInternalWriter();
    Map newattrs = null;
    XMLLump close = lump.close_tag;
    XMLLump endopen = lump.open_end;
    newattrs = rewriteURLs(lump, newattrs);
   
    xmlw.writeRaw(lump.parent.buffer, lump.start, lump.length);
    if (newattrs == null) {
      RenderUtil.dumpTillLump(lump.parent.lumps, lump.lumpindex + 1, endopen.lumpindex + 1, pos);
    }
    else {
      newattrs.remove(XMLLump.ID_ATTRIBUTE);
      XMLUtil.dumpAttributes(newattrs, xmlw);
      if (endopen == close) {
        pos.print("/>");
      }
      else {
        pos.print(">");
//        RenderUtil.dumpTillLump(lumps, endopen.lumpindex + 1,
//            close.lumpindex + 1, pos);
      }
    }
    return ComponentRenderer.NESTING_TAG;
  }

}
