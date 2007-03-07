/*
 * Created on 10-Feb-2006
 */
package uk.org.ponder.rsf.flow.jsfnav;

import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;

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
  /** A default NavigationCase which operates for any action return **/
  public NavigationCase(AnyViewParameters resultingView) {
    this.resultingView = resultingView;
  }

  public NavigationCase(AnyViewParameters resultingView, String flowCondition) {
    this.resultingView = resultingView;
    this.flowCondition = flowCondition;
  }
  
  public NavigationCase(String fromOutcome, AnyViewParameters resultingView) {
    this.fromOutcome = fromOutcome;
    this.resultingView = resultingView;
  }
  
  public NavigationCase(String fromOutcome, AnyViewParameters resultingView, String flowCondition) {
    this.fromOutcome = fromOutcome;
    this.resultingView = resultingView;
    this.flowCondition = flowCondition;
  }

  public String fromOutcome;
  public AnyViewParameters resultingView;
  /**
   * "Flow condition marker", defaulting to
   * {@link uk.org.ponder.rsf.flow.ARIResult}.FLOW_END. Set to FLOW_START,
   * PROPAGATE or FLOW_ONESTEP for other flow behaviour.
   */
  public String flowCondition = ARIResult.FLOW_END;
 
}
