/*
 * Created on 10-Feb-2006
 */
package uk.org.ponder.rsf.flow.jsfnav;

import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * Summarises a single "navigation case", a static rule mapping from the return
 * value of an RSF Action into the ViewParameters to be navigated to. Used by
 * the "JSF Navigation Style" ActionResultInterpreter,
 * {@link uk.org.ponder.rsf.flow.jsfnav.JSFNavActionResultInterpreter}.
 * 
 * This has been extended from the JSF model to include a single extra field,
 * the "flow condition marker" which determines flow propagation state of parts
 * of the bean model marked as managed by a
 * {@link uk.org.ponder.rsf.preservation.StatePreservationStrategy}.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class NavigationCase {
  public NavigationCase() {
  }

  public NavigationCase(String fromoutcome, AnyViewParameters toViewId) {
    this.fromOutcome = fromoutcome;
    this.toViewId = toViewId;
  }
  
  public NavigationCase(String fromOutcome, AnyViewParameters toViewId, String flowCondition) {
    this.fromOutcome = fromOutcome;
    this.toViewId = toViewId;
    this.flowCondition = flowCondition;
  }

  public String fromOutcome;
  public AnyViewParameters toViewId;
  /**
   * "Flow condition marker", defaulting to
   * {@link uk.org.ponder.rsf.flow.ARIResult}.FLOW_END. Set to FLOW_START,
   * PROPAGATE or FLOW_ONESTEP for other flow behaviour.
   */
  public String flowCondition = ARIResult.FLOW_END;
 
}
