/*
 * Created on May 11, 2006
 */
package uk.org.ponder.rsf.renderer;

import java.util.Map;

import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.rsf.uitype.UITypes;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.xml.XMLUtil;
import uk.org.ponder.xml.XMLWriter;

/** The context necessary for a ComponentRenderer to do its work */

public class TagRenderContext {
  public boolean isEmpty() {
    return endopen.lumpindex == close.lumpindex;
  }

  public Map attrcopy;
  public XMLLump uselump;
  public XMLLump endopen;
  public XMLLump close;
  public PrintOutputStream pos;
  public XMLWriter xmlw;
  public boolean iselide;

  public int nextpos;

  public TagRenderContext(Map attrcopy, XMLLump uselump, XMLLump endopen,
      XMLLump close, PrintOutputStream pos, XMLWriter xmlw, int nextpos,
      boolean iselide) {
    this.attrcopy = attrcopy;
    this.uselump = uselump;
    this.endopen = endopen;
    this.close = close;
    this.pos = pos;
    this.xmlw = xmlw;
    this.nextpos = nextpos;
    this.iselide = iselide;
  }

  public void closeTag() {
    if (!iselide) {
      pos.print("</");
      pos.write(uselump.parent.buffer, uselump.start + 1, uselump.length - 2);
      pos.print(">");
    }
  }

  public void renderUnchanged() {
    RenderUtil.dumpTillLump(uselump.parent.lumps, uselump.lumpindex + 1,
        close.lumpindex + (iselide ? 0 : 1), pos);
  }

  public void rewriteLeaf(String value) {
    if (value != null && !UITypes.isPlaceholder(value))
      replaceBody(value);
    else
      replaceAttributes();
  }

  public void rewriteLeafOpen(String value) {
    if (iselide) {
      rewriteLeaf(value);
    }
    else {
      if (value != null && !UITypes.isPlaceholder(value))
        replaceBody(value);
      else
        replaceAttributesOpen();
    }
  }

  public void replaceBody(String value) {
    XMLUtil.dumpAttributes(attrcopy, xmlw);
    if (!iselide) {
      pos.print(">");
    }
    xmlw.write(value);
    closeTag();
  }

  public void replaceAttributes() {
    if (!iselide) {
      XMLUtil.dumpAttributes(attrcopy, xmlw);
    }

    dumpTemplateBody();
  }

  public void replaceAttributesOpen() {
    if (iselide) {
      replaceAttributes();
    }
    else {
      XMLUtil.dumpAttributes(attrcopy, xmlw);
      pos.print(isEmpty() ? "/>" : ">");

      nextpos = endopen.lumpindex + 1;
    }
  }

  public void dumpTemplateBody() {
    if (isEmpty()) {
      if (!iselide) {
        pos.print("/>");
      }
    }
    else {
      if (!iselide) {
        pos.print(">");
      }
      RenderUtil.dumpTillLump(uselump.parent.lumps, endopen.lumpindex + 1,
          close.lumpindex + (iselide ? 0 : 1), pos);
    }
  }

}
