/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.hashutil.EighteenIDGenerator;
import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.FlowIDHolder;
import uk.org.ponder.rsf.flow.errors.ActionErrorStrategy;
import uk.org.ponder.rsf.request.ActionTarget;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;

/**
 * One instance of this bean is declared per flow in the request context. The
 * "invoke action" phase of processing attempts to invoke a method on this bean
 * named for the "action" method specified in the flow configuration, which is
 * here decoded and converted into the required method call (or sequence of
 * calls) on the beans specified in the relevant action states.
 * <p>
 * These beans are created in FlowProxyFactory, a useful convenience bean that
 * prevents the user from needing to set the other tedious dependencies of this
 * bean in addition to the actual target flow object.
 * <p>
 * Note that this bean causes the FlowIDHolder contents to be set with the
 * respective flowID if it receives a START transition. This is a somewhat
 * violation of BeanReasonableness, but we have not yet an architecture for flow
 * scope construct-time activities.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class FlowActionProxyBean implements ActionTarget {
  // less hassle to keep this here than manage as a dependency....
  private static EighteenIDGenerator idgenerator = new EighteenIDGenerator();
  private Flow flow;
  private ActionErrorStrategy actionerrorstrategy;

  private ReflectiveCache reflectivecache;
  private BeanLocator rbl;
  private ViewParameters viewparams;

  private FlowIDHolder flowidholder;

  private boolean strict = false;

  // These two are user configured properties
  public void setFlow(Flow flow) {
    this.flow = flow;
  }

  public void setStrict(boolean strict) {
    this.strict = strict;
  }

  // The remainder are system configured properties
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

  /**
   * Called in response to invocation of a command link. The "method" name
   * actually corresponds to the "on" text of the VIEW state. If the method name
   * is FLOW_START, and the first state is a VIEW, we actually perform no action
   * but simply redirect to the view, while noting that we want to start a flow.
   * Note that the value returned from this method will be *immediately* fed to
   * interpretActionResult above. If on the other hand we hit an ACTION state,
   * we follow the chain, invoking actions as we go until we do hit a view
   * state. The name of the resulting view state is the return.
   */
  public Object invokeAction(String name, String knownresult) {
    State newstate;
    ARIResult togo = new ARIResult();
    togo.resultingview = viewparams.copyBase();

    if (name.equals(ARIResult.FLOW_START)) {
      if (!flowidholder.isEmpty()) {
        throw new IllegalStateException("Flow " + flowidholder
            + " already in progress, " + "cannot be started");
      }
      flowidholder.setFlowID(flow.id);
      flowidholder.setFlowToken(idgenerator.generateID());
      newstate = flow.stateFor(flow.startstate);
      if (newstate instanceof ViewState) {
        flowidholder.setFlowStateID(newstate.id);
        flowidholder.setRequestFlowStateID(newstate.id);

        togo.propagatebeans = ARIResult.FLOW_START;

      }
    }
    else { // any action other than FLOW_START
      if (flowidholder.getRequestFlowStateID() == null) {
        throw new IllegalStateException("Received flow action " + name
            + " for Flow " + flow.id + " without current flow state");
      }
      if (viewparams.endflow != null) {
        throw new IllegalStateException("Received flow action " + name
        + " for ended flow " + flow.id);
      }
      if (strict
          && !flowidholder.getFlowStateID().equals(
              flowidholder.getRequestFlowStateID())) {
        throw new IllegalStateException("Flow " + flowidholder
            + " received request" + " for state "
            + flowidholder.getRequestFlowStateID() + " whereas current "
            + " flow state is " + flowidholder.getFlowStateID());
      }
      ViewState viewstate = (ViewState) flow.stateFor(flowidholder
          .getRequestFlowStateID());
      Transition trans = viewstate.transitions.transitionOn(name);
      if (trans == null) {
        throw new IllegalArgumentException(
            "Could not find transition from viewstate with ID "
                + viewparams.viewID + " for command " + name);
      }
      newstate = flow.stateFor(trans.to);
    }

    // continue while still actions - stop at either View or an EndState
    while (newstate instanceof ActionState) {
      ActionState actionstate = (ActionState) newstate;
      Action action = actionstate.action;
      Object bean = rbl.locateBean(action.bean);
      if (bean == null) {
        throw new IllegalStateException("Bean with id " + action.bean
            + " not found in for action " + actionstate.id);
      }
      String result = null;
      Exception exception = null;
      if (knownresult == null) {
        try {
          result = (String) reflectivecache.invokeMethod(bean, action.method);
        }
        catch (Exception e) {
          exception = e;
        }
      }
      else {
        result = knownresult;
        knownresult = null; // consume the known result on the first occasion.
      }
      actionerrorstrategy.handleError(result, exception, flowidholder
          .getRequestFlowStateID(), viewparams.viewID);

      Transition trans2 = actionstate.transitions.transitionOn(result);
      newstate = flow.stateFor(trans2.to);
      Logger.log.info("Transition from action state " + actionstate.id
          + " to state " + newstate.id + " through result of " + result);
    }
    ViewableState viewstate = (ViewableState) newstate;
    flowidholder.setRequestFlowStateID(newstate.id);
    flowidholder.setFlowStateID(newstate.id);
    togo.resultingview.viewID = viewstate.viewID;
    togo.resultingview.flowtoken = flowidholder.getFlowToken();

    if (togo.propagatebeans == null) { // if not filled in as FLOW_START

      togo.propagatebeans = newstate instanceof EndState ? ARIResult.FLOW_END
          : ARIResult.PROPAGATE;
    }
    return togo;
  }
}
