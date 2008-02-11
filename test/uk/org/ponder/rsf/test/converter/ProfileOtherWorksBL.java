/*
 * Created on 11 Feb 2008
 */
package uk.org.ponder.rsf.test.converter;

import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.beanutil.support.ConcreteWBL;

public class ProfileOtherWorksBL extends ConcreteWBL {

  public ProfileOtherWorksBL() {
    WriteableBeanLocator map4 = new ConcreteWBL();
    map4.set("3", "Value to be deleted");
    set("4", map4);
  }
  
}
