/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.processor;

import java.util.Map;

import uk.org.ponder.errorutil.CoreMessages;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
import uk.org.ponder.rsf.state.RSVCApplier;
import uk.org.ponder.rsf.state.ErrorStateManager;
import uk.org.ponder.rsf.state.RequestSubmittedValueCache;
import uk.org.ponder.rsf.state.StatePreservationManager;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.RunnableWrapper;

/**
 * PostHandler is a request
 *         scope bean responsible for handling an HTTP POST request, or other
 *         non-idempotent web service "action" cycle.
 * @author Antranig Basman (antranig@caret.cam.ac.uk) 
 */
public class PostHandler {
  // application-scope dependencies
  private ActionResultInterpreter ari;
  private RunnableWrapper postwrapper;
  private RequestSubmittedValueCache requestrsvc;

  // request-scope dependencies
  private Map normalizedmap;
  private ViewParameters viewparams;
  private ErrorStateManager errorstatemanager;
  private RSVCApplier rsvcapplier;
  private StatePreservationManager presmanager; // no, not that of OS/2

  public void setErrorStateManager(ErrorStateManager errorstatemanager) {
    this.errorstatemanager = errorstatemanager;
  }

  public void setActionResultInterpreter(ActionResultInterpreter ari) {
    this.ari = ari;
  }

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  public void setRequestRSVC(RequestSubmittedValueCache requestrsvc) {
    this.requestrsvc = requestrsvc;
  }

  public void setAlterationWrapper(RunnableWrapper postwrapper) {
    this.postwrapper = postwrapper;
  }

  public void setRSVCApplier(RSVCApplier rsvcapplier) {
    this.rsvcapplier = rsvcapplier;
  }
  
  public void setNormalizedRequestMap(Map normalizedmap) {
    this.normalizedmap = normalizedmap;
  }

  public void setStatePreservationManager(StatePreservationManager presmanager) {
    this.presmanager = presmanager;
  }
  
  // Since this entire bean is request scope, there is no difficulty with
  // letting
  // the action result escape from the wrapper into this instance variable.
  private String actionresult = null;

  public ViewParameters handle() {
  
    final String actionmethod = PostDecoder.decodeAction(normalizedmap);

    try {
      // invoke all state-altering operations within the runnable wrapper.
      postwrapper.wrapRunnable(new Runnable() {
        public void run() {
          if (viewparams.flowtoken != null) {
            presmanager.restore(viewparams.flowtoken);
          }
          rsvcapplier.applyValues(requestrsvc); // many errors possible here.

          if (actionmethod != null) {
            actionresult = rsvcapplier.invokeAction(actionmethod);
          }
        }
      }).run();
    }
    catch (Exception e) {
      Logger.log.error(e);
      ThreadErrorState.addError(new TargettedMessage(
          CoreMessages.GENERAL_ACTION_ERROR));
    }

    String submitting = PostDecoder.decodeSubmittingControl(normalizedmap);
    errorstatemanager.globaltargetid = submitting;

    ARIResult arires = ari.interpretActionResult(viewparams, actionresult);
    if (!arires.propagatebeans.equals(ARIResult.FLOW_END)) {
      // TODO: consider whether we want to allow ARI to allocate a NEW TOKEN
      // for a FLOW FORK.
      if (arires.resultingview.flowtoken == null) {
        // if the ARI wanted one and hasn't allocated one, allocate flow token.
        arires.resultingview.flowtoken = errorstatemanager.allocateToken();
      }
      presmanager.preserve(viewparams.flowtoken);
    }
    else {
      if (viewparams.flowtoken != null) {
        presmanager.clear(viewparams.flowtoken);
      }
    }
    
    String errortoken = errorstatemanager.requestComplete();
    
    arires.resultingview.errortoken = errortoken;
    return arires.resultingview;
  }

}