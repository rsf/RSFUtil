/*
 * Created on 5 Jun 2008
 */
package uk.org.ponder.rsf.test.vpi;

import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.RawViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsInterceptor;

public class RawVPI implements ViewParamsInterceptor {

  public AnyViewParameters adjustViewParameters(ViewParameters incoming) {
    return new RawViewParameters("http://www.google.com");
  }

}
