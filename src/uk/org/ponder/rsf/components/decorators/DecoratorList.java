/*
 * Created on May 16, 2006
 */
package uk.org.ponder.rsf.components.decorators;

import java.util.ArrayList;

/** A typesafe list of UIDecorator objects **/

public class DecoratorList extends ArrayList {  
  public DecoratorList() {}
  public DecoratorList(UIDecorator single) {
    add(single);
  }
  
  public UIDecorator decoratorAt(int i) {
    return (UIDecorator) get(i);
  }
  
  public boolean add(Object o) {
    // ensure ClassCastException for non-decorators
    UIDecorator toadd = (UIDecorator) o;
    return super.add(toadd);
  }
}
