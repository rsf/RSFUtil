/*
 * Created on May 15, 2006
 */
package uk.org.ponder.rsf.request;

import uk.org.ponder.streamutil.write.PrintOutputStream;

public class InternalRedirectState {
  public EarlyRequestParser redirectparser;
  public PrintOutputStream outstream;
  
  public InternalRedirectState(EarlyRequestParser redirectparser, PrintOutputStream outstream) {
    this.redirectparser = redirectparser;
    this.outstream = outstream;
  }
  
}
