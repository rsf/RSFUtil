/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.renderer;

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.request.FossilizedConverter;
import uk.org.ponder.rsf.template.XMLLump;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.stringutil.URLEncoder;
import uk.org.ponder.stringutil.URLUtil;
import uk.org.ponder.util.Logger;
import uk.org.ponder.xml.XMLUtil;
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
  /** Dump from template to output until either we reduce below <code>basedepth</code>
   * recursion level, or we hit an rsf:id, or end of file. Return the lump index
   * we reached. This has two uses, firstly from the base of the main scanning loop,
   * and secondly from the "glue" scanning. The main scanning loop runs until we reduce
   * BELOW RECURSION LEVEL OF PARENT, i.e. we output its closing tag and then return.
   * The glue loop requires that we DO NOT OUTPUT THE CLOSING TAG OF PARENT because
   * we may have some number of repetitive components still to render.
   */
  public static int dumpScan(XMLLump[] lumps, int renderindex, int basedepth,  
      PrintOutputStream target, boolean closeparent) {
    int start = lumps[renderindex].start;
    char[] buffer = lumps[renderindex].buffer;
    while (true) {
      if (renderindex == lumps.length) break;
      XMLLump lump = lumps[renderindex];
      if (lump.rsfID != null || lump.nestingdepth < basedepth) break;
//      target.print(lump.text);
      ++renderindex;
    }
    // ASSUMPTIONS: close tags are ONE LUMP
    if (!closeparent && lumps[renderindex].rsfID == null) --renderindex;
    int limit = (renderindex == lumps.length? buffer.length : lumps[renderindex].start);

    target.write(buffer, start, limit - start);
    return renderindex;
  }
  
  

  public static void dumpHiddenField(String name, String value, XMLWriter xmlw) {
    xmlw.writeRaw("<input type=\"hidden\" ");
    XMLUtil.dumpAttribute("name", name, xmlw);
    XMLUtil.dumpAttribute("value", value, xmlw);
    xmlw.writeRaw(" />\n");
  }
  
  public static String appendAttributes(String baseurl, String attributes) {
    // Replace a leading & by ? in the attributes, if there are no
    // existing attributes in the URL
    // TODO: hop into the URL before any anchors
    if (baseurl.indexOf('?') == -1 && attributes.length() > 0) {
      attributes = "?" + attributes.substring(1);
    }
    return baseurl + attributes;
  }
  
  public static String makeURLAttributes(ParameterList params) {
    CharWrap togo = new CharWrap();
    for (int i = 0; i < params.size(); ++ i) {
      UIParameter param = params.parameterAt(i);
      togo.append("&").append(URLEncoder.encode(param.name)).append("=").
        append(URLEncoder.encode(param.value));
    }
    return togo.toString();
  }

  /** "Unpacks" the supplied command link "name" (as encoded using the 
   * HTMLRenderSystem for submission controls) by treating it as a section
   * of URL attribute stanzas. The key/value pairs encoded in it will be
   * added to the supplied (modifiable) map.
   */ 
  public static void unpackCommandLink(String longvalue, Map requestparams) {
    String[] split = longvalue.split("[&=]");
    // start at 1 since string will begin with &
    if ((split.length %2) == 0) {
      Logger.log.warn("Erroneous submission - odd number of parameters/values in " + longvalue);
      return;
    }
    for (int i = 1; i < split.length; i += 2) {
      String key = URLUtil.decodeURL(split[i]);
      String value = URLUtil.decodeURL(split[i + 1]);
      Logger.log.info("Unpacked command link key " + key + " value " + value);
      String[] existing = (String[]) requestparams.get(key);
      if (existing == null) {
        requestparams.put(key, new String[] {value});
      }
      else {
        String[] fused = (String[]) ArrayUtil.append(existing, value);
        requestparams.put(key, fused);
      }
    }
    
  }
  public static String findCommandParams(Map requestparams) {
    for (Iterator parit = requestparams.keySet().iterator(); parit.hasNext();) {
      String key = (String) parit.next();
      if (key.startsWith(FossilizedConverter.COMMAND_LINK_PARAMETERS)) return key;
    }
    return null;
  }
}
