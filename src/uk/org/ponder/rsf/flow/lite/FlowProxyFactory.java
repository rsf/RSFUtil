/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class FlowProxyFactory {
  private ReflectiveCache reflectivecache;
  private BeanLocator rbl;
  private ViewParameters viewparams;
  
  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    this.reflectivecache = reflectivecache;
  }

  public void setBeanLocator(BeanLocator rbl) {
    this.rbl = rbl;
  }

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }
  
  public FlowProxy getFlow()  {
    FlowProxy togo = new FlowProxy();
    togo.setBeanLocator(rbl);
    togo.setReflectiveCache(reflectivecache);
    togo.setViewParameters(viewparams);
    return togo;
  }

}
