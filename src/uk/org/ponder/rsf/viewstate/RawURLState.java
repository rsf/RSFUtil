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
  
  public String pathinfo;
  public Map params;
  public String anchor;
}
