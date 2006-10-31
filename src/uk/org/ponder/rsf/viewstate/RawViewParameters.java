/*
 * Created on 31 Oct 2006
 */
package uk.org.ponder.rsf.viewstate;

/** A form of ViewParameters representing a URL external to RSF. This is legal
 * only for outgoing references (e.g. from NavigationCaseReporter) within RSF,
 * and will not be reported as the ViewParameters for any view.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class RawViewParameters implements AnyViewParameters {
  public String URL;
  public RawViewParameters(String URL) {
    this.URL = URL;
  }
  
  public String toString() {
    return URL;
  }
}
