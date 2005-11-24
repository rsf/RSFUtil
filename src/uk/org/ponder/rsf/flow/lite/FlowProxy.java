/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.reflect.MethodInvokingProxy;
import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;

public class FlowProxy implements MethodInvokingProxy {
  private Flow flow;
  
  private ReflectiveCache reflectivecache;
  private BeanLocator rbl;
  private ViewParameters viewparams;

  public void setFlow(Flow flow) {
    this.flow = flow;
  }
  
  public void setReflectiveCache(ReflectiveCache reflectivecache) {
    this.reflectivecache = reflectivecache;
  }

  public void setBeanLocator(BeanLocator rbl) {
    this.rbl = rbl;
  }

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }
  
  /**
   * Called in response to invocation of a command link. The "method" name
   * actually corresponds to the "on" text of the VIEW state. If the method name
   * FLOW_START, and the first state is a VIEW, we actually perform no action
   * but simply redirect to the view, while noting that we want to start a flow.
   * Note that the value returned from this method will be *immediately* fed to
   * interpretActionResult above. If on the other hand we hit an ACTION state,
   * we follow the chain, invoking actions as we go until we do hit a view
   * state. The name of the resulting view state is the return.
   */
  public Object invokeMethod(String name, Object[] args) {
    ViewState viewstate;
    if (name.equals(ARIResult.FLOW_START)) {
      State start = flow.stateFor(flow.startstate);
      if (start instanceof ViewState) {
        return ARIResult.FLOW_START;
      }
      else {
        viewstate = (ViewState) start;
      }
    }
    else {
      viewstate = (ViewState) flow.stateFor(viewparams.viewID);
    }
    Transition trans = viewstate.transitions.transitionOn(name);
    if (trans == null) {
      throw new IllegalArgumentException(
          "Could not find transition from viewstate with ID "
              + viewparams.viewID + " for command " + name);
    }
    State newstate = flow.stateFor(trans.to);
    while (newstate instanceof ActionState) {
      ActionState actionstate = (ActionState) newstate;
      Action action = actionstate.action;
      Object bean = rbl.locateBean(action.bean);
      if (bean == null) {
        throw new IllegalStateException("Bean with id " + action.bean
            + " not found in for action " + actionstate.id);
      }
      String result = (String) reflectivecache
          .invokeMethod(bean, action.method);
      Transition trans2 = actionstate.transitions.transitionOn(result);
      newstate = flow.stateFor(trans2.to);
      Logger.log.info("Transition from action state " + actionstate.id
          + " to state " + newstate.id + " through result of " + result);
    }
    return newstate.id;
  }
}
