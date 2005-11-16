/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.List;

import uk.org.ponder.beanutil.WriteableBeanLocator;

public class StatePreservationManager {
  private List strategies;
  public void setStrategies(List strategies) {
    this.strategies = strategies;
  }
  
  private WriteableBeanLocator wbl;
  
  public void setWriteableBeanLocator(WriteableBeanLocator wbl) {
    this.wbl = wbl;
  }
  
  StatePreservationStrategy strategyAt(int i) {
    return (StatePreservationStrategy) strategies.get(i);
  }
  
  public void preserve(String tokenid) {
    
    for (int i = 0; i < strategies.size(); ++ i) {
      strategyAt(i).preserve(wbl, tokenid);
    }
  }
  
  public void restore(String tokenid) {
    for (int i = 0; i < strategies.size(); ++ i) {
      strategyAt(i).restore(wbl, tokenid);
    }
  }

  public void clear(String tokenid) {
    for (int i = 0; i < strategies.size(); ++ i) {
      strategyAt(i).clear(tokenid);
    }
  }
}
