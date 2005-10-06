/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer.html;

import java.util.HashMap;

import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.renderer.StaticComponentRenderer;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.util.URLRewriter;
import uk.org.ponder.rsf.view.ViewTemplate;
import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.xml.XMLWriter;

public class URLRewriteSCR implements StaticComponentRenderer {
  private URLRewriter rewriter;
  private String relpath;

  public String getName() {
    return "rewrite-url";
  }

  public void setURLRewriter(URLRewriter resolver) {
    this.rewriter = resolver;
  }
  
  public void setViewTemplate(ViewTemplate template) {
    this.relpath = template.getRelativePath();
  }
  
  // returns null if no change required.
  public HashMap getResolvedURLMap(HashMap attrs, String name) {
    if (rewriter == null)
      return null;
    String toresolve = (String) attrs.get(name);
    String resolved = rewriter.rewriteResourceURL(toresolve, relpath);
    if (resolved == null) {
      return null;
    }
    else {
      HashMap attrcopy = (HashMap) attrs.clone();
      attrcopy.put(name, resolved);
      return attrcopy;
    }

  }

  public int render(XMLLump[] lumps, int lumpindex, XMLWriter xmlw) {
    PrintOutputStream pos = xmlw.getInternalWriter();
    HashMap newattrs = null;
    XMLLump lump = lumps[lumpindex];
    XMLLump close = lump.close_tag;
    XMLLump endopen = lump.open_end;
    if ((lump.textEquals("<a ") || lump.textEquals("<link "))
        && lump.attributemap.containsKey("href")) {
      newattrs = getResolvedURLMap(lump.attributemap, "href");
    }
    if (lump.textEquals("<img ") && lump.attributemap.containsKey("src")) {
      newattrs = getResolvedURLMap(lump.attributemap, "src");
    }
    xmlw.writeRaw(lump.buffer, lump.start, lump.length);
    if (newattrs == null) {
      RenderUtil.dumpTillLump(lumps, lumpindex + 1, close.lumpindex + 1, pos);
    }
    else {
      RenderUtil.dumpAttributes(newattrs, xmlw);
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
