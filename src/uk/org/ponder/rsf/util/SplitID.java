/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.util;

import uk.org.ponder.stringutil.CharWrap;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class SplitID {
  public static final String WILDCARD_COMPONENT = "*";
  public static final char SEPARATOR = ':';
  public String prefix;
  public String suffix;
  public SplitID(String id) {
    int colpos = id.indexOf(SEPARATOR);
    if (colpos == -1) {
      prefix = id;
    }
    else {
      prefix = id.substring(0, colpos);
      suffix = id.substring(colpos + 1);
    }
  }
  public String toString() {
    CharWrap togo = new CharWrap(prefix.length() + suffix.length() + 1);
    togo.append(prefix);
    if (suffix != null) {
      togo.append(SEPARATOR).append(suffix);
    }
    return togo.toString();
  }
  
  public static boolean isSplit(String id) {
    return id.indexOf(SEPARATOR) != -1;
  }
  
  public static String getSuffix(String id) {
    int colpos = id.indexOf(SEPARATOR);
    return (colpos == -1) ? null :
      id.substring(colpos + 1);
  }
  
  /** Returns the "raw" prefix of the ID - i.e. the part before the colon
   * if one appears, or the entire ID if none.
   */
  public static String getPrefix(String id) {
    int colpos = id.indexOf(SEPARATOR);
    return colpos == -1? id : id.substring(0, colpos);
  }
  
  /** Returns the "repetitive" prefix of the ID - i.e. the prefix including
   * colon if one appears, or null if none.
   */
  public static String getPrefixColon(String id) {
    int colpos = id.indexOf(SEPARATOR);
    return colpos == -1? null : id.substring(0, colpos + 1);
  }
}
