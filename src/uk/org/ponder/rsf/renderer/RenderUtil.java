/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.renderer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.rsf.state.SubmittedValueEntry;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.util.Logger;
import uk.org.ponder.xml.XMLWriter;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RenderUtil {
 
  public static int dumpTillLump(XMLLump[] lumps, int start, int limit, PrintOutputStream target) {
//    for (; start < limit; ++ start) {
//      target.print(lumps[start].text);
//    }
    target.write(lumps[start].buffer, lumps[start].start, lumps[limit].start - lumps[start].start);
    return limit;
  }
  
  public static int dumpScan(XMLLump[] lumps, int renderindex, int basedepth,  
      PrintOutputStream target) {
    int start = lumps[renderindex].start;
    char[] buffer = lumps[renderindex].buffer;
    while (true) {
      if (renderindex == lumps.length) break;
      XMLLump lump = lumps[renderindex];
      if (lump.rsfID != null || lump.nestingdepth < basedepth) break;
//      target.print(lump.text);
      ++renderindex;
    }
    int limit = (renderindex == lumps.length? buffer.length : lumps[renderindex].start);
    target.write(buffer, start, limit - start);
    return renderindex;
  }

  public static void dumpAttribute(String name, String value, XMLWriter xmlw) {
    xmlw.writeRaw(" ").writeRaw(name).writeRaw("=\"");
    xmlw.write(value);
    xmlw.writeRaw("\"");
  }
  
  public static void dumpHiddenField(String name, String value, XMLWriter xmlw) {
    xmlw.writeRaw("<input type=\"hidden\" ");
    dumpAttribute("name", name, xmlw);
    dumpAttribute("value", value, xmlw);
    xmlw.writeRaw(" />\n");
  }
  
  public static void dumpAttributes(Map attrs, XMLWriter xmlw) {
    for (Iterator keyit = attrs.keySet().iterator(); keyit.hasNext(); ) {
      String key = (String) keyit.next();
        String attrvalue = (String) attrs.get(key);
        dumpAttribute(key, attrvalue, xmlw);
    }
  }
  
  public static String makeURLAttributes(Map attrs) {
    CharWrap togo = new CharWrap();
    for (Iterator keyit = attrs.keySet().iterator(); keyit.hasNext();) {
      String key = (String) keyit.next();
      String value = (String) attrs.get(key);
      togo.append("&").append(key).append("=").append(value);
    }
    return togo.toString();
  }

  public static void unpackCommandLink(String value, HashMap requestparams) {
    String[] split = value.split("[&=]");
    // start at 1 since string will begin with &
    for (int i = 1; i < split.length; i += 2) {
      Logger.log.info("Unpacked command link key " + split[i] + " value " + split[i + 1]);
      requestparams.put(split[i], new String[] {split[i + 1]});
    }
    
  }
  public static String findCommandParams(Map requestparams) {
    for (Iterator parit = requestparams.keySet().iterator(); parit.hasNext();) {
      String key = (String) parit.next();
      if (key.startsWith(SubmittedValueEntry.COMMAND_LINK_PARAMETERS)) return key;
    }
    return null;
  }
}
