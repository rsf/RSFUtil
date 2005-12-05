/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.rsf.flow.ActionErrorStrategy;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Given a Flow object (as read from disk or constructed) return a FlowProxy
 * bean capable of intercepting attempted method invokation requests as part
 * of the "invoke application" phase. Principally exists as a developer 
 * convenience to avoid setting these dependencies on such proxy.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class FlowProxyFactory {
  private ReflectiveCache reflectivecache;
  private BeanLocator rbl;
  private ViewParameters viewparams;
  private FlowIDHolder flowidholder;
  private ActionErrorStrategy actionerrorstrategy;
  
  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    this.reflectivecache = reflectivecache;
  }

  public void setBeanLocator(BeanLocator rbl) {
    this.rbl = rbl;
  }

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }
  
  public void setFlowIDHolder(FlowIDHolder flowidholder) {
    this.flowidholder = flowidholder;
  }
  
  public void setActionErrorStrategy(ActionErrorStrategy actionerrorstrategy) {
    this.actionerrorstrategy = actionerrorstrategy;
  }
  
  public FlowActionProxyBean getFlowProxy()  {
    FlowActionProxyBean togo = new FlowActionProxyBean();
    togo.setBeanLocator(rbl);
    togo.setReflectiveCache(reflectivecache);
    togo.setViewParameters(viewparams);
    togo.setFlowIDHolder(flowidholder);
    togo.setActionErrorStrategy(actionerrorstrategy);
    return togo;
  }

}
