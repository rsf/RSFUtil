/*
 * Created on 9 Nov 2006
 */
package uk.org.ponder.rsf.builtin;

import uk.org.ponder.beanutil.BeanGetter;

public class UVBBean {
  private BeanGetter rbg;
  
  public void setRequestBeanGetter(BeanGetter rbg) {
    this.rbg = rbg;
  }
  public Object[] values;
  
  public String[] paths;
  
  /** The action method, after all bindings are complete, reads a collection
   * of EL paths.
   */
  
  public void populate() {
    values = new Object[paths.length];
    for (int i = 0; i < paths.length; ++ i) {
      values[i] = rbg.getBean(paths[i]);
    }
  }
  
}
