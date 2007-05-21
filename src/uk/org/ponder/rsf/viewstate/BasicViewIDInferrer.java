/*
 * Created on 11 Jul 2006
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

/** The RSF default ViewIDInferrer which simply adopts the first section
 * of pathInfo up to the first /.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
// TODO: this should ultimately connect up with ViewParamsMapInfo for some kind
// of global inference strategy.
public class BasicViewIDInferrer implements ViewIDInferrer {

  public String inferViewID(String pathinfo, Map requestmap) {
    int firstslashpos = pathinfo.indexOf('/', 1);
    String viewID = firstslashpos == -1 ? pathinfo.substring(1)
        : pathinfo.substring(1, firstslashpos);
    return viewID;
  }

}
