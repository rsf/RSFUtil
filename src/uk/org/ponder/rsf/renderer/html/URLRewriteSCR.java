/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer.html;

import java.util.HashMap;

import uk.org.ponder.htmlutil.HTMLConstants;
import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.renderer.StaticComponentRenderer;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.view.ViewTemplate;
import uk.org.ponder.rsf.viewstate.URLRewriter;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.xml.XMLUtil;
import uk.org.ponder.xml.XMLWriter;

/** Performs a URL-rewrite for a template-relative
 * "resource" URL (e.g. image or CSS) as found in a template.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class URLRewriteSCR implements StaticComponentRenderer {
  public static final String NAME = "rewrite-url";
  private URLRewriter rewriter;
  private String resourcebase;

  public String getName() {
    return NAME;
  }

  public void setURLRewriter(URLRewriter resolver) {
    this.rewriter = resolver;
  }

  public void setViewTemplate(ViewTemplate template) {
    this.resourcebase = template.getResourceBase();
  }

  public String resolveURL(String toresolve) {
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
  public HashMap getResolvedURLMap(HashMap attrs, String name, HashMap cloned) {
    String toresolve = (String) attrs.get(name);
    if (toresolve == null || rewriter == null) return cloned;
    String resolved = resolveURL(toresolve);
    if (resolved == null) {
      return cloned;
    }
    else {
      HashMap togo = null;
      if (cloned == null) {
        togo = (HashMap) attrs.clone();
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

  public int render(XMLLump[] lumps, int lumpindex, XMLWriter xmlw) {
    PrintOutputStream pos = xmlw.getInternalWriter();
    HashMap newattrs = null;
    XMLLump lump = lumps[lumpindex];
    XMLLump close = lump.close_tag;
    XMLLump endopen = lump.open_end;
    String linkattr = getLinkAttribute(lump);
    if (linkattr != null) {
      newattrs = getResolvedURLMap(lump.attributemap, linkattr, newattrs);
    }
    for (int i = 0; i < HTMLConstants.ubiquitousURL.length; ++i) {
      newattrs = getResolvedURLMap(lump.attributemap,
          HTMLConstants.ubiquitousURL[i], newattrs);
    }
    xmlw.writeRaw(lump.buffer, lump.start, lump.length);
    if (newattrs == null) {
      RenderUtil.dumpTillLump(lumps, lumpindex + 1, close.lumpindex + 1, pos);
    }
    else {
      newattrs.remove(XMLLump.ID_ATTRIBUTE);
      XMLUtil.dumpAttributes(newattrs, xmlw);
      if (endopen == close) {
        pos.print("/>");
      }
      else {
        pos.print(">");
        RenderUtil.dumpTillLump(lumps, endopen.lumpindex + 1,
            close.lumpindex + 1, pos);
      }
    }
    return ComponentRenderer.LEAF_TAG;
  }

}
