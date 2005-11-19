/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.rsf.viewstate.ViewParameters;

public interface ViewExceptionStrategy {
  public ViewParameters handleException(Exception e, ViewParameters incoming);
}
