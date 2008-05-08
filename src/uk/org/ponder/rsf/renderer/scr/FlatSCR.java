/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.rsf.renderer.scr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uk.org.ponder.rsf.renderer.ComponentRenderer;
import uk.org.ponder.rsf.renderer.RenderUtil;
import uk.org.ponder.rsf.renderer.TagRenderContext;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.xml.NameValue;
import uk.org.ponder.xml.XMLUtil;

/** Class encapsulating a "static" (i.e. independent of any producer
 * components) rewriting operation on a tag, which is "flat" - that is, 
 * operates on a predetermined attribute based on static data.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class FlatSCR implements BasicSCR {
  /** A value for the <code>body_strategy</code> field, indicating that this
   * renderer will append to any existing tag body that it discovers.
   */
  public static final String APPEND_BODY = "append body";
  /** A value for the <code>body_strategy</code> field, indicating that this
   * renderer will replace any existing tag body that it discovers.
   */
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
  /** A method for deserialisation */
  public Iterator getNameValue() {
    ArrayList values = new ArrayList();
    for (Iterator vit = attributemap.keySet().iterator(); vit.hasNext();) {
      String key = (String) vit.next();
      values.add(new NameValue(key, (String) attributemap.get(key)));
    }
    return values.iterator();
  }
 
  public int render(TagRenderContext trc) {
    
    if (tag != null) {
      trc.pos.print("<").print(tag).print(" ");
    }
    else {
      trc.pos.write(trc.uselump.parent.buffer, trc.uselump.start, trc.uselump.length);
    }
    trc.attrcopy.putAll(attributemap);
    trc.attrcopy.remove(XMLLump.ID_ATTRIBUTE);
    XMLUtil.dumpAttributes(trc.attrcopy, trc.xmlw);
    if (trc.isEmpty() && body == null) {
      trc.pos.print("/>");
    }
    else {
      trc.pos.print(">");
      if (tag_type == ComponentRenderer.LEAF_TAG) {
        if (body != null && body_strategy.equals(REPLACE_BODY)) {
          trc.pos.print(body);
          trc.pos.write(trc.close.parent.buffer, trc.close.start, trc.close.length);
        }
        else {
          if (body != null) { 
            trc.pos.print(body);
            RenderUtil.dumpTillLump(trc.uselump.parent.lumps, trc.endopen.lumpindex + 1,
             trc.close.lumpindex + 1, trc.pos);
          }
        } // end if complete body replacement
      } // end if leaf tag
    }
    return tag_type;
  }
}
