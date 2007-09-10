/*
 * Created on 21 May 2007
 */
package uk.org.ponder.rsf.viewstate.support;

import java.util.List;

import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/** Coordinates all the DefaultViewsInferrers in the system **/

public class DefaultViewInferrerManager implements ViewParamsReporter {

  private List inferrers;
  
  public void setInferrers(List inferrers) {
    this.inferrers = inferrers;
  }
  
  public ViewParameters getViewParameters() {
    for (int i = 0; i < inferrers.size(); ++ i) {
      ViewParamsReporter reporter = (ViewParamsReporter) inferrers.get(i);
      ViewParameters params = reporter.getViewParameters();
      if (params != null) return params;
    }
    return null;
  }
  
}
