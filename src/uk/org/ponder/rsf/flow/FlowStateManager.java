/*
 * Created on 6 Aug 2006
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.rsf.preservation.StatePreservationManager;
import uk.org.ponder.rsf.state.ErrorStateManager;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Manages adjustment of ARIResult fields and scopes to take account of
 * changes in flow state. Relatively tightly coupled to RSFActionHandler,
 * since control is shared of the state of StatePreservationManager.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */ 

public class FlowStateManager {
  private ErrorStateManager errorstatemanager;
  private StatePreservationManager presmanager; // no, not that of OS/2
  
  public void setErrorStateManager(ErrorStateManager errorstatemanager) {
    this.errorstatemanager = errorstatemanager;
  }

  public void setStatePreservationManager(StatePreservationManager presmanager) {
    this.presmanager = presmanager;
  }
  
  public void inferFlowState(ViewParameters viewparams, ARIResult ariresult) {
    
    String prop = ariresult.propagateBeans; // propagation code for this cycle
    // An external URL must naturally end any flow
    if (!(ariresult.resultingView instanceof ViewParameters)) {
      if (viewparams.flowtoken != null) {
        presmanager.flowEnd(viewparams.flowtoken);
      }
      return;
    }
    ViewParameters newview = (ViewParameters) ariresult.resultingView;

    if (!prop.equals(ARIResult.FLOW_END)
        && !prop.equals(ARIResult.FLOW_ONESTEP)) {
      // TODO: consider whether we want to allow ARI to allocate a NEW TOKEN
      // for a FLOW FORK. Some call this, "continuations".
      if (newview.flowtoken == null) {
        if (prop.equals(ARIResult.FLOW_START)
            || prop.equals(ARIResult.FLOW_FASTSTART)) {
          // if the ARI wanted one and hasn't allocated one, allocate flow
          // token.
          newview.flowtoken = errorstatemanager.allocateToken();
        }
        else { // else assume existing flow continues.
          if (viewparams.flowtoken == null) {
            throw new IllegalStateException(
                "Cannot propagate flow state without active flow");
          }
          newview.flowtoken = viewparams.flowtoken;
        }
      }
      // On a FLOW_START, **ONLY** the flow state itself is to be saved,
      // since any other existing bean state will be non-flow or end-flow.
      presmanager.preserve(newview.flowtoken, prop
          .equals(ARIResult.FLOW_START));
    }
    else if (prop.equals(ARIResult.FLOW_ONESTEP)) {
      if (viewparams.endflow != null || viewparams.flowtoken != null) {
        throw new IllegalStateException(
            "Cannot use one-step flow from previous flow state");
      }
      newview.flowtoken = errorstatemanager.allocateToken();
      newview.endflow = "1";
      presmanager.flowEnd(newview.flowtoken);
    }
    else { // it is a flow end.
      if (viewparams.flowtoken != null) {
        newview.endflow = "1";
        newview.flowtoken = viewparams.flowtoken;
        presmanager.flowEnd(viewparams.flowtoken);
      }
    }
  }
}
