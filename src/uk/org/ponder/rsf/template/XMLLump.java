/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.template;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.arrayutil.ArrayUtil;

/**
 * A primitive "lump" of an XML document, representing a "significant" 
 * character span. The basic function is to hold indexes start, length into
 * the character array for the document, as well as various housekeeping 
 * information to aid navigation and debugging.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class XMLLump {
  public int lumpindex;
  public int line, column;
  public int nestingdepth;
  
  public XMLViewTemplate parent;
 
  public int start, length;
  public String rsfID;
  public XMLLump open_end;        // lump containing " >"
  public XMLLump close_tag;       // lump containing "</close">
  public XMLLump uplump;
  // open and close will be the same for empty tag case " />"
  // headlump has standard text of |<tagname | to allow easy identification.
  public XMLLumpMMap downmap;
  
  // potentially present on *every* tag - maps branch names to their final close lump 
  private Map endmap;
  // map from attribute name to lump where value occurs.
  // this may be reformed to map to text if we collapse attribute lumps?
  // this is a HashMap so that the fast clone method is easily accessible
  public HashMap attributemap;
  // the (XHTML) attribute appearing in the template file designating a 
  // template component. 
  public static final String ID_ATTRIBUTE = "rsf:id";
  /** The prefix indicating an rsf:id for a 
   * {@link uk.org.ponder.rsf.renderer.scr.StaticComponentRenderer}
   */
  public static final String SCR_PREFIX = "scr=";
  /** The prefix indicating an rsf:id for a simple internationalised message **/
  public static final String MSG_PREFIX = "msg=";  
  /** Value for rsf:id that represents a CollectingSCR */
  public static final String SCR_CONTRIBUTE_PREFIX = "scr=contribute-";
  // A value for the rsf:id attribute indicating that the actual (leaf) component
  // to be targetted by component rendering is somewhere inside the component
  // holding the ID. NEVER issue a component with this ID! In this case, the
  // component holding the ID will be either a div or a span.
  public static final String PAYLOAD_COMPONENT = "payload-component";
  // this occurs in the SAME CONTAINER scope as the target???
  public static final String FORID_PREFIX = "message-for:";// + SplitID.SEPARATOR;
  public static final String FORID_SUFFIX = "*";
  /** This prefix for an rsf:id will elide the surrounding tag when rendered. This
   * cannot be combined with an empty body*/
  public static final String ELISION_PREFIX = "~";
  public XMLLump() {}
  public XMLLump(int lumpindex, int nestingdepth) {
    this.lumpindex = lumpindex;
    this.nestingdepth = nestingdepth;
  }
  public XMLLump getDownHolder() {
    XMLLump move = this;
    while (move != null && move.downmap == null) move = move.uplump;
    return move;
  }
  public XMLLump getFinal(String id) {
    return (XMLLump) (endmap == null? null : endmap.get(id));
  }

  public void setFinal(String ID, XMLLump lump) {
    if (endmap == null) {
      endmap = new HashMap(8);
    }
    endmap.put(ID, lump);
  }
  
  public boolean isTag(String tag) {
    return parent.buffer[start] == '<' && parent.buffer[start + length - 1] == ' '
       && ArrayUtil.equals(tag, parent.buffer, start + 1, length - 2);
  }
  
  public boolean textEquals(String tocheck) {
    return ArrayUtil.equals(tocheck, parent.buffer, start, length);
  }
  public static String tagToText(String tagname) {
    return "<" + tagname + " ";
  }
  public static String textToTag(String text) {
    return text.substring(1, text.length() - 1);
  }
  public String getTag() {
    return new String(parent.buffer, start + 1, length - 2);
  }
  
  public String toDebugString() {
    return "lump line " + line + " column " + column +" index " + lumpindex;
  }
  public String toString() {
    return new String(parent.buffer, start, length) + " at " + toDebugString() 
    + (parent.fullpath == null? "" : " in file " + parent.fullpath);
  }
}
