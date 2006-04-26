/*
 * Created on Nov 2, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.ArrayList;

/** A type-safe list of UIParameter objects */

public class ParameterList extends ArrayList {
  public UIParameter parameterAt(int i) {
    return (UIParameter) get(i);
  }
  public boolean add(Object o) {
    UIParameter cast = (UIParameter) o;
    return super.add(cast);
  }
}
