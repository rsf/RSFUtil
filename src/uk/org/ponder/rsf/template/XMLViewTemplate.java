/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.template;

import java.io.InputStream;
import java.util.HashMap;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.rsf.util.SplitID;
import uk.org.ponder.rsf.view.ViewTemplate;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * The parser for the IKAT view template, implemented using the XPP3 "rapid"
 * XML pull parser. After parsing, this representation of the template is 
 * discarded, in favour of its raw constituents, being i) The XMLLump[] array,
 * ii) The "root lump" holding the initial downmap, and iii) the global headmap.
 * The system assumes that renders will be much more frequent than template reads,
 * and so takes special efforts to condense the representation for rapid render-time
 * access, at the expense of slightly slower parsing. TODO: fix this ridiculous
 * dependency mixup - parsing code should be OUTSIDE and state should be INSIDE!
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class XMLViewTemplate implements ViewTemplate {
  public int INITIAL_LUMP_SIZE = 1000;
  public XMLLump rootlump;
  public XMLLumpMMap globalmap;
  
  //private HashMap foridtocomponent = new HashMap();
  
  public XMLLump[] lumps;
  private CharWrap buffer;

  public boolean hasComponent(String ID) {
    return globalmap.hasID(ID);
  }

  private void writeToken(int token, XmlPullParser parser, CharWrap w) {

    char[] chars = parser.getTextCharacters(limits);
    switch (token) {
    case XmlPullParser.COMMENT:
      w.append("<!--");
      break;
    case XmlPullParser.ENTITY_REF:
      w.append("&");
      break;
    case XmlPullParser.CDSECT:
      w.append("<![CDATA[");
      break;
    case XmlPullParser.PROCESSING_INSTRUCTION:
      w.append("<?");
      break;
    case XmlPullParser.DOCDECL:
      w.append("<!DOCTYPE");
      break;

    }
    w.append(chars, limits[0], limits[1]);
    switch (token) {
    case XmlPullParser.COMMENT:
      w.append("-->");
      break;
    case XmlPullParser.ENTITY_REF:
      w.append(";");
      break;
    case XmlPullParser.CDSECT:
      w.append("]]>");
      break;
    case XmlPullParser.PROCESSING_INSTRUCTION:
      w.append("?>");
      break;
    case XmlPullParser.DOCDECL:
      w.append(">");
      break;
    }
  }
  
  private void setLumpChars(XMLLump lump, char[] chars, int start, int length) {
    lump.start = buffer.size;
    lump.length = length;
    buffer.append(chars, start, length);
  }  

  private void setLumpString(XMLLump lump, String string) {
    lump.start = buffer.size;
    lump.length = string.length();
    buffer.append(string);
  }

  private void simpleTagText(XmlPullParser parser) {
    char[] chars = parser.getTextCharacters(limits);
    setLumpChars(lumps[lumpindex - 1], chars, limits[0], limits[1]);
//    String text = new String(chars, limits[0], limits[1]);
//    lumps[lumpindex - 1].text = text;
  }

  private void processDefaultTag(int token, XmlPullParser parser) {
    CharWrap w = new CharWrap();
    writeToken(token, parser, w);
    setLumpChars(lumps[lumpindex - 1], w.storage, 0, w.size);
//    lumps[lumpindex - 1].text = w.toString();
  }

  private void processTagStart(XmlPullParser parser, boolean isempty) {
    XMLLump headlump = lumps[lumpindex - 1];
    String tagname = parser.getName();
    // standard text of |<tagname | to allow easy identification.
    setLumpString(headlump, XMLLump.tagToText(tagname));
    //HashMap forwardmap = new HashMap();
    //headlump.forwardmap = forwardmap;
    // current policy - every open tag gets a forwardmap, and separate lumps.
    // eventually we want only thing with an ID as such.
    int attrs = parser.getAttributeCount(); 
    if (attrs > 0) {
      headlump.attributemap = new HashMap();
    }
    for (int i = 0; i < attrs; ++i) {
      String attrname = parser.getAttributeName(i);
      String attrvalue = parser.getAttributeValue(i);
      XMLLump frontlump = newLump(parser);
      CharWrap lumpac = new CharWrap();
      if (i > 0) {
        lumpac.append("\" ");
      }
      lumpac.append(attrname).append("=\"");
      setLumpChars(frontlump, lumpac.storage, 0, lumpac.size);
      // frontlump holds |" name="|
      // valuelump just holds the value.
      // This is a bit silly since we have all attrs in attrmap, but just
      // MARGINALLY
      // quicker to iterate over them this way!
      XMLLump valuelump = newLump(parser);
      setLumpString(valuelump, attrvalue);
      headlump.attributemap.put(attrname, attrvalue);

      if (attrname.equals(XMLLump.ID_ATTRIBUTE)) {
        String ID = attrvalue;
        headlump.rsfID = attrvalue;
        SplitID split = new SplitID(ID);
        
        XMLLump stacktop = findTopContainer();     
        stacktop.downmap.addLump(ID, headlump);
        globalmap.addLump(ID, headlump);
        
        //if (split.prefix.equals(HTMLLump.FORID_PREFIX)) {
        //  foridtocomponent.put(split.suffix == null ? HTMLLump.FORID_PREFIX
        //      : split.suffix, headlump);
          // we need really to be able to locate 3 levels of id -
          // for-message:message:to
          // ideally we would also like to be able to locate repetition
          // constructs too, hopefully the standard suffix-based computation
          // will allow this. However we previously never allowed BOTH
          // repetitious and non-repetitious constructs to share the same
          // prefix, so revisit this to solve.
        //}
        if (split.suffix != null) {
          // a repetitive tag is found.
          headlump.downmap = new XMLLumpMMap();

          // Repetitions within a SCOPE should be UNIQUE and CONTIGUOUS.
          XMLLump prevlast = stacktop.downmap.getFinal(split.prefix);
          stacktop.downmap.setFinal(split.prefix, headlump);
          if (prevlast != null) {
            // only store transitions from non-initial state - 
            // TODO: see if transition system will ever be needed. 
          String prevsuffix = SplitID.getSuffix(prevlast.rsfID);
          String transitionkey = split.prefix + SplitID.SEPARATOR + prevsuffix + XMLLump.TRANSITION_SEPARATOR
              + split.suffix;
          stacktop.downmap.addLump(transitionkey, prevlast);
          globalmap.addLump(transitionkey, prevlast);
          }
        }
      }
    }
    XMLLump finallump = newLump(parser);
    
    String closetext = attrs == 0? (isempty? "/>" : ">"):(isempty? "\"/>" : "\">");
    setLumpString(finallump, closetext);
    headlump.open_end = finallump;

    tagstack.add(nestingdepth, headlump);
    if (isempty) {
      processTagEnd(parser);
    }
  }

  private void processTagEnd(XmlPullParser parser) {
    //String tagname = parser.getName();
    XMLLump oldtop = tagstack.lumpAt(nestingdepth);
    
    oldtop.close_tag = lumps[lumpindex - 1];
    tagstack.remove(nestingdepth);
  }
  
  private XMLLump findTopContainer() {
    for (int i = tagstack.size() - 1; i >= 0; --i) {
      XMLLump lump = tagstack.lumpAt(i);
      if (lump.rsfID != null && lump.rsfID.indexOf(SplitID.SEPARATOR) != -1) return lump;
    }
    return rootlump;
  }

  // temporary array for getCharacterText
  private int[] limits = new int[2];
  private int lumpindex = 0;
  private int nestingdepth = 0;
  // only stores repetitive tags.
  private XMLLumpList tagstack = new XMLLumpList();

  private XMLLump newLump(XmlPullParser parser) {
    if (lumpindex == lumps.length) {
      lumps = (XMLLump[]) ArrayUtil.expand(lumps, 2.0);
    }
    XMLLump togo = new XMLLump(lumpindex, nestingdepth);
    togo.line = parser.getLineNumber();
    togo.column = parser.getColumnNumber();
    lumps[lumpindex] = togo;
    ++lumpindex;
    return togo;
  }


  // XPP tag depths:
  //  <!-- outside --> 0
  //  <root> 1
  //    sometext 1
  //      <foobar> 2
  //      </foobar> 2
  //  </root> 1
  //  <!-- outside --> 0

  public void init() {
    lumps = new XMLLump[INITIAL_LUMP_SIZE];
    buffer = new CharWrap(INITIAL_LUMP_SIZE * 10);
    lumpindex = 0;
    tagstack.clear();
    rootlump = new XMLLump();
    rootlump.downmap = new XMLLumpMMap();
    rootlump.nestingdepth = -1;
    globalmap = new XMLLumpMMap();
  }
  
  public void parse(InputStream xmlstream) {
    long time = System.currentTimeMillis();
    init();
    XmlPullParser parser = new MXParser();
    try {
      //parser.setFeature(FEATURE_XML_ROUNDTRIP, true);
      parser.setInput(xmlstream, null);
      while (true) {
        int token = parser.nextToken();
        if (token == XmlPullParser.END_DOCUMENT) break;
        // currently 1 lump for each token - an optimisation would collapse
        // provable irrelevant lumps. but watch out for end tags! Some might
        // be fused, some not.
        nestingdepth = parser.getDepth() - 1;
        XMLLump lump = newLump(parser);

        switch (token) {
        case XmlPullParser.START_TAG:
          boolean isempty = parser.isEmptyElementTag();
          processTagStart(parser, isempty);
          if (isempty) {
            parser.next();
          }
          break;
        case XmlPullParser.END_TAG:
          simpleTagText(parser);
          processTagEnd(parser);
          break;
        default:
          processDefaultTag(token, parser);
        }
      }

    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t, "Error parsing template");
    }
    endParse();
    //Logger.log.info("Template parsed in " + (System.currentTimeMillis() - time) + "ms");
  }

  private void endParse() {
    lumps = (XMLLump[]) ArrayUtil.trim(lumps, lumpindex);
    tagstack.clear();
    char[] compacted = new char[buffer.size];
    System.arraycopy(buffer.storage, 0, compacted, 0, buffer.size);
    buffer = null;
    for (int i = 0; i < lumps.length; ++ i) {
      lumps[i].buffer = compacted;
    }
  }
  
  private String relativepath;
  
  public void setRelativePath(String relativepath) {
   this.relativepath = relativepath;
  }
  public String getRelativePath() {
    return relativepath;
  }
  
}