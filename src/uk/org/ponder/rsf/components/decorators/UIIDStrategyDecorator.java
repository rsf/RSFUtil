/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.components.decorators;

public class UIIDStrategyDecorator extends UIDecorator {
  public String IDStrategy;
  
  public UIIDStrategyDecorator(String IDStrategy) {
    this.IDStrategy = IDStrategy;
  }
  
  public UIIDStrategyDecorator() {}
}
