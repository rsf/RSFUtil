/*
 * Created on 4 Mar 2008
 */
package uk.org.ponder.rsf.test.navigation;

import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class ELParams extends SimpleViewParameters {
  public String el;
  
  public ELParams() {}
  
  public ELParams(String el) {
    this.viewID = RequestLauncher.TEST_VIEW;
    this.el = el;
  }
}
