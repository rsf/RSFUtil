/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.xml.NameValue;
import uk.org.ponder.xml.XMLWriter;

/** Class encapsulating a "static" (i.e. independent of any producer
 * components) rewriting operation on a tag. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class BasicSCR implements StaticComponentRenderer {
  public static final String APPEND_BODY = "append body";
  public static final String REPLACE_BODY = "replace body";
  /** A replacement tag, if tag is to be rewritten - this is currently only
   * supported for empty tags. May be null for no action.
   */
  public String tag;
  /** An attribute map to be applied on top of any existing attributes */
  private HashMap attributemap = new HashMap(); 
  /** The name of this renderer, to form as index - it will be invoked by the
   * renderer on seeing an attribute rsf-id="scr:[name]";
   */
  private String name;
  /** Takes on one of the two static values above, indicating strategy to be
   * used with the supplied body text.
   */
  public String body_strategy = null;
  /** Any text to form as the body of the tag, null if no replacement required.
   * May only be set if the renderer type is LEAF_TAG. */
  public String body;
  /** One of the values from ComponentRenderer, indicating whether this 
   * renderer is intended to act on complete tags, consuming the close tag from
   * the template stream.
   */
  public int tag_type = ComponentRenderer.LEAF_TAG;
  
  public void addNameValue(NameValue toadd) {
    attributemap.put(toadd.name, toadd.value);
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  // deserialisation method - fix this system at some point. Cannot understand
  // any more how maps are supported by the SAXalizer.
  public Iterator getNameValue() {
    ArrayList values = new ArrayList();
    for (Iterator vit = attributemap.keySet().iterator(); vit.hasNext();) {
      String key = (String) vit.next();
      values.add(new NameValue(key, (String) attributemap.get(key)));
    }
    return values.iterator();
  }
 
  public int render(XMLLump[] lumps, int lumpindex, XMLWriter xmlw) {
    PrintOutputStream pos = xmlw.getInternalWriter();
    
    XMLLump lump = lumps[lumpindex];
    XMLLump close = lump.close_tag;
    XMLLump endopen = lump.open_end;
    if (tag != null) {
      pos.print("<").print(tag).print(" ");
    }
    else {
      pos.print(lump.text);
    }
    HashMap newattrs = new HashMap();
    newattrs.putAll(lump.attributemap);
    newattrs.putAll(attributemap);
    RenderUtil.dumpAttributes(newattrs, xmlw);
    if (endopen == close && body == null) {
      pos.print("/>");
    }
    else {
      pos.print(">");
      if (tag_type == ComponentRenderer.LEAF_TAG) {
        if (body != null && body_strategy.equals(REPLACE_BODY)) {
          pos.print(body);
          pos.print(close.text);
        }
        else {
          if (body != null) { 
            pos.print(body);
            RenderUtil.dumpTillLump(lumps, endopen.lumpindex + 1,
            close.lumpindex + 1, pos);
          }
        } // end if complete body replacement
      } // end if leaf tag
    }
    return tag_type;
  }
}
