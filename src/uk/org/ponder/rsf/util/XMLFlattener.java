/*
 * Created on Aug 12, 2005
 */
package uk.org.ponder.rsf.util;

import java.io.Reader;
import java.io.StringReader;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;

import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.streamutil.write.StringPOS;
import uk.org.ponder.util.UniversalRuntimeException;

public class XMLFlattener {
  public final static String FEATURE_XML_ROUNDTRIP = "http://xmlpull.org/v1/doc/features.html#xml-roundtrip";

  public static void flatten(Reader xmlstream, PrintOutputStream pos) {
    XmlPullParser parser = new MXParser();
    try {
      parser.setFeature(FEATURE_XML_ROUNDTRIP, true);
      parser.setInput(xmlstream);
      //int[] limits = new int[2];
      while (true) {
        int token = parser.nextToken();
        if (token == XmlPullParser.END_DOCUMENT)
          break;
        if (token == XmlPullParser.TEXT || token == XmlPullParser.ENTITY_REF) {
//          char[] chars = parser.getTextCharacters(limits);
//          String text = new String(chars, limits[0], limits[1]);
          String text = parser.getText();
          pos.print(text);
        }
      }
    }
    catch (Exception e) {
      throw UniversalRuntimeException.accumulate(e,
          "Error flattening HTML document");
    }
  }
  public static String flatten(String toflatten) {
    StringPOS pos = new StringPOS();
    StringReader reader = new StringReader(toflatten);
    flatten(reader, pos);
    pos.close();
    String togo = pos.toString();
    return togo;
  }
}
