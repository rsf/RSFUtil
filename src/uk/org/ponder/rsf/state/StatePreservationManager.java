/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.ArrayList;
import java.util.List;

import uk.org.ponder.beanutil.WriteableBeanLocator;

public class StatePreservationManager {
  private List strategies;
  private List flowendstrategies;

  public void setStrategies(List strategies) {
    this.strategies = strategies;
  }

  public void setFlowEndStrategies(List flowendstrategies) {
    this.flowendstrategies = flowendstrategies;
  }

  private WriteableBeanLocator wbl;

  public void setWriteableBeanLocator(WriteableBeanLocator wbl) {
    this.wbl = wbl;
  }

  private StatePreservationStrategy strategyAt(int i) {
    return (StatePreservationStrategy) strategies.get(i);
  }

  private StatePreservationStrategy endStrategyAt(int i) {
    return (StatePreservationStrategy) flowendstrategies.get(i);
  }

  public void init() {
    if (strategies == null) {
      strategies = new ArrayList(0);
    }
    if (flowendstrategies == null) {
      flowendstrategies = new ArrayList(0);
    }
  }

  public void preserve(String tokenid) {
    for (int i = 0; i < strategies.size(); ++i) {
      strategyAt(i).preserve(wbl, tokenid);
    }
  }

  /** Request all registered strategies to restore state into the current
   * request-scope container.
   * @param The flow token keying the state to be restored.
   * @param flowend <code>true</code> if the specified flow has expired.
   */
  public void restore(String tokenid, boolean flowend) {
    if (flowend) {
      for (int i = 0; i < strategies.size(); ++i) {
        strategyAt(i).restore(wbl, tokenid);
      }
    }
    else {
      for (int i = 0; i < flowendstrategies.size(); ++i) {
        endStrategyAt(i).restore(wbl, tokenid);
      }
    }
  }

  /**
   * Signal that the current flow (multi-request sequence) has ended. Remove all
   * state from flow preservation strategies, and transfer to flow-end
   * strategies (if any).
   */
  public void flowEnd(String tokenid) {
    for (int i = 0; i < strategies.size(); ++i) {
      strategyAt(i).clear(tokenid);
    }
    for (int i = 0; i < flowendstrategies.size(); ++i) {
      endStrategyAt(i).preserve(wbl, tokenid);
    }
  }
}
