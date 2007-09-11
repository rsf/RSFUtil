/*
 * Created on 11 Sep 2007
 */
package uk.org.ponder.rsf.view.support;

import uk.org.ponder.rsf.view.ViewGroup;
import uk.org.ponder.stringutil.StringList;

/** The parsed form of a viewID or contentType predicate from a {@link ViewGroup} 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ParsedPredicate {
  public boolean positive = true;
  public String[] elements;
  
  public static ParsedPredicate parse(String toparse) {
    ParsedPredicate togo = new ParsedPredicate();
    if (toparse == null || toparse.length() == 0) return togo;
    if (toparse.charAt(0) == '!') {
      togo.positive = false;
    }
    togo.elements = StringList.fromString(toparse).toStringArray();
    return togo;
  }
}
