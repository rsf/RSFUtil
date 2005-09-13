/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.util.Logger;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RenderUtil {

  public static int dumpTillLump(HTMLLump[] lumps, int start, int limit, PrintOutputStream target) {
    for (; start < limit; ++ start) {
      target.print(lumps[start].text);
    }
    return limit;
  }
  
  public static int dumpScan(HTMLLump[] lumps, int renderindex, int basedepth,  
      PrintOutputStream target) {
    while (true) {
      if (renderindex == lumps.length) break;
      HTMLLump lump = lumps[renderindex];
      if (lump.rsfID != null || lump.nestingdepth < basedepth) break;
      target.print(lump.text);
      ++renderindex;
    }
    return renderindex;
  }

  public static void dumpAttribute(String name, String value, PrintOutputStream pos) {
    pos.print(" ").print(name).print("=\"");
    // TODO: remember to XML-encode this!!
    pos.print(value).print("\"");
  }
  
  public static void dumpHiddenField(String name, String value, PrintOutputStream pos) {
    pos.print("<input type=\"hidden\" name=\"");
    pos.print(name).print("\" value=").print(value).println("\" />");
  }
  
  public static void dumpAttributes(Map attrs, PrintOutputStream pos) {
    for (Iterator keyit = attrs.keySet().iterator(); keyit.hasNext(); ) {
      String key = (String) keyit.next();
        String attrvalue = (String) attrs.get(key);
        dumpAttribute(key, attrvalue, pos);
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
    for (int i = 0; i < split.length; i += 2) {
      Logger.log.info("Unpacked command link key " + split[i] + " value " + split[i + 1]);
      requestparams.put(split[i], split[i + 1]);
    }
    
  }
}
