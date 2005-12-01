/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import java.util.Iterator;

public class FlowUtil {

  public static void validateFlow(Flow flow) {
    if (flow.id == null) {
      throw new IllegalArgumentException("Flow has no defined id");
    }
    if (flow.startstate == null || flow.stateFor(flow.startstate) == null) {
      throw new IllegalArgumentException("Flow with ID " + flow.id + " has no valid start state: " +
          flow.startstate);
    }
    for (Iterator it = flow.getStates().iterator(); it.hasNext(); ) {
      State state = (State) it.next();
      if (state instanceof ViewState) {
        validateTransitions(flow, ((ViewState)state).transitions, ViewState.class);
      }
      else if (state instanceof ActionState) {
        validateTransitions(flow, ((ActionState)state).transitions, ActionState.class); 
      }
    }
  }

  private static void validateTransitions(Flow flow, TransitionList list, Class source) {
    for (int i = 0; i < list.size(); ++ i) {
      String to = list.transitionAt(i).to;
      State tostate = flow.stateFor(to);
      if (to == null) {
        throw new IllegalArgumentException("Target state " + to + " of transition not found");
      }
  
    }
  }
}
