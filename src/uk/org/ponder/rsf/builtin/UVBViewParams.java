/*
 * Created on 8 Nov 2006
 */
package uk.org.ponder.rsf.builtin;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class UVBViewParams extends SimpleViewParameters {
  public String[] fetchbeans;

  public String getParseSpec() {
    return super.getParseSpec() + ",fetchbeans";
  }

}
