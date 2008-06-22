/*
 * Created on 23 Jun 2008
 */
package uk.org.ponder.rsf.test.badparams;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class BadParams1 extends SimpleViewParameters {

  public String isbn;
  
  public String getParseSpec() {
    return super.getParseSpec() + ",isbn:@1";
  }
}
