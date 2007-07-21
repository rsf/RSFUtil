/*
 * Created on 16 Jul 2007
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

/** The raw transport format for URL information to and from a {@link ViewParamsCodec}
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class RawURLState {
  public RawURLState(Map params, String pathinfo) {
    this.params = params;
    this.pathinfo = pathinfo;
  }
  public RawURLState() {}
  /** The "pathinfo" segment of the URL, which starts with a leading slash (/). 
   * This may be null for parsing, but must be set for rendering.
   */
  public String pathinfo;
  /** The (equivalent) request parameter map, a map of String to String[]. 
   * This must be set.*/
  public Map params;
  /** An optional anchor, to be emitted following the URL with #, if supported */ 
  public String anchor;
}
