/*
 * Created on 23 Jun 2008
 */
package uk.org.ponder.rsf.test.badparams;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class BadParams2 extends SimpleViewParameters {

  public Trunk params = new Trunk();
  
  public String getParseSpec() {
    return super.getParseSpec() + ",params.*:";
  }
}
