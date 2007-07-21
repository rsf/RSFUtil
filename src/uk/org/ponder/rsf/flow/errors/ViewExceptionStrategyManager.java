/*
 * Created on 18 May 2007
 */
package uk.org.ponder.rsf.flow.errors;

import java.util.List;

import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ViewExceptionStrategyManager implements ViewExceptionStrategy {
  private List strategies;

  public void setStrategyList(List strategies) {
    this.strategies = strategies;
  }

  public AnyViewParameters handleException(Exception e, ViewParameters incoming) {
    for (int i = 0; i < strategies.size(); ++ i) {
      ViewExceptionStrategy strategy = (ViewExceptionStrategy) strategies.get(i);
      AnyViewParameters result = strategy.handleException(e, incoming);
      if (result != null) return result;
    }
    return null;
  }
  
  
  
}
