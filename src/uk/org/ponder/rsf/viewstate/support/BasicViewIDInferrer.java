/*
 * Created on 11 Jul 2006
 */
package uk.org.ponder.rsf.viewstate.support;

import java.util.Map;

import uk.org.ponder.rsf.viewstate.StaticViewIDInferrer;
import uk.org.ponder.rsf.viewstate.ViewParamUtil;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.stringutil.StringUtil;

/** The RSF default ViewIDInferrer which adopts a fixed strategy of inferring
 * the View ID from a request environment.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
// TODO: this should ultimately connect up with ViewParamsMapInfo for some kind
// of global inference strategy.
public class BasicViewIDInferrer implements StaticViewIDInferrer {

  private String viewIDspec = "@0";
  
  public String inferViewID(String pathinfo, Map requestmap) {
    if (viewIDspec.startsWith(ViewParameters.TRUNK_PARSE_PREFIX)) {
      int index = Integer.parseInt(viewIDspec.substring(ViewParameters.TRUNK_PARSE_PREFIX.length()));
      Object high0 = requestmap.get(ViewParamUtil.getAttrIndex(index, true));
      if (high0 != null) {
        return high0 instanceof String? (String)high0 : ((String[]) high0)[0];
      }
      String[] segments = StringUtil.split(pathinfo, '/', false);
      boolean slash0 = pathinfo.charAt(0) == '/';
      return segments[index + (slash0? 1 : 0)];
    }
    else {
      Object attr = requestmap.get(viewIDspec);
      return attr instanceof String? (String)attr : ((String[]) attr)[0];
    }
  }

  public String getViewIDSpec() {
    return viewIDspec;
  }
  
  public void setViewIDSpec(String viewIDspec) {
    this.viewIDspec = viewIDspec;
  }

}
