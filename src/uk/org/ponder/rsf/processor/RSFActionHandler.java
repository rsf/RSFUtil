/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.processor;

import java.util.Map;

import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.rsf.flow.ARIResolver;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
import uk.org.ponder.rsf.flow.errors.ActionErrorStrategy;
import uk.org.ponder.rsf.flow.errors.ViewExceptionStrategy;
import uk.org.ponder.rsf.preservation.StatePreservationManager;
import uk.org.ponder.rsf.request.RequestSubmittedValueCache;
import uk.org.ponder.rsf.state.ErrorStateManager;
import uk.org.ponder.rsf.state.RSVCApplier;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.RunnableWrapper;

/**
 * ActionHandler is a request scope bean responsible for handling an HTTP POST
 * request, or other non-idempotent web service "action" cycle. Defines the core
 * logic for this processing cycle.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class RSFActionHandler implements ActionHandler {
  // application-scope dependencies
  private ARIResolver ariresolver;
  private RunnableWrapper postwrapper;
  private RequestSubmittedValueCache requestrsvc;

  // request-scope dependencies
  private Map normalizedmap;
  private ViewParameters viewparams;
  private ErrorStateManager errorstatemanager;
  private RSVCApplier rsvcapplier;
  private StatePreservationManager presmanager; // no, not that of OS/2
  private ViewExceptionStrategy ves;
  private ActionErrorStrategy actionerrorstrategy;

  public void setErrorStateManager(ErrorStateManager errorstatemanager) {
    this.errorstatemanager = errorstatemanager;
  }

  public void setARIResolver(ARIResolver ariresolver) {
    this.ariresolver = ariresolver;
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

  public void setViewExceptionStrategy(ViewExceptionStrategy ves) {
    this.ves = ves;
  }

  public void setActionErrorStrategy(ActionErrorStrategy actionerrorstrategy) {
    this.actionerrorstrategy = actionerrorstrategy;
  }  

  private ARIResult ariresult = null;

  /**
   * The result of this post cycle will be of interest to some other request
   * beans, in particular the alteration wrapper. This bean must however be
   * propagated lazily since it is only constructed partway through this
   * handler, if indeed it is a POST cycle at all.
   * 
   * @return
   */
  public ARIResult getARIResult() {
    return ariresult;
  }

  // Since this entire bean is request scope, there is no difficulty with
  // letting the action result escape from the wrapper into this instance
  // variable.
  private Object actionresult = null;

  private Object handleError(Object actionresult, Exception exception,
      TargettedMessageList messages) {
    // an ARIResult is at the end-of-line.
    if (actionresult != null && !(actionresult instanceof String))
      return actionresult;
    Object newcode = actionerrorstrategy.handleError((String) actionresult,
        exception, null, viewparams.viewID);
    if (newcode != null && !(newcode instanceof String))
      return actionresult;
    for (int i = 0; i < messages.size(); ++i) {
      TargettedMessage message = messages.messageAt(i);
      if (message.exception != null) {
        newcode = actionerrorstrategy.handleError((String) newcode,
            message.exception, null, viewparams.viewID);
      }
    }
    return newcode;
  }

  public ViewParameters handle() {
    ThreadErrorState.beginRequest();
    final TargettedMessageList errors = ThreadErrorState.getErrorState().errors;
    final String actionmethod = PostDecoder.decodeAction(normalizedmap);

    try {
      // invoke all state-altering operations within the runnable wrapper.
      postwrapper.wrapRunnable(new Runnable() {
        public void run() {
          if (viewparams.flowtoken != null) {
            presmanager.restore(viewparams.flowtoken,
                viewparams.endflow != null);
          }
          Exception exception = null;
          try {
            rsvcapplier.applyValues(requestrsvc); // many errors possible here.
          }
          catch (Exception e) {
            exception = e;
          }
          Object newcode = handleError(actionresult, exception, errors);
          exception = null;
          if (newcode == null || newcode instanceof String) {
            // only proceed to actually invoke action if no ARIResult already
            // note all this odd two-step procedure is only required to be able
            // to
            // pass AES error returns and make them "appear" to be the returns
            // of
            // the first action method in a Flow.
            try {
              if (actionmethod != null) {
                actionresult = rsvcapplier.invokeAction(actionmethod,
                    (String) newcode);
              }
            }
            catch (Exception e) {
              exception = e;
            }
            newcode = handleError(actionresult, exception, errors);
          }
          if (newcode != null)
            actionresult = newcode;
          // must interpret ARI INSIDE the wrapper, since it may need it
          // on closure.
          if (actionresult instanceof ARIResult) {
            ariresult = (ARIResult) actionresult;
          }
          else {
            ActionResultInterpreter ari = ariresolver
                .getActionResultInterpreter();
            ariresult = ari.interpretActionResult(viewparams, actionresult);
          }
        }
      }).run();
      String prop = ariresult.propagatebeans; // propagation code for this cycle
      ViewParameters newview = ariresult.resultingview;

      if (!prop.equals(ARIResult.FLOW_END)
          && !prop.equals(ARIResult.FLOW_ONESTEP)) {
        // TODO: consider whether we want to allow ARI to allocate a NEW TOKEN
        // for a FLOW FORK. Some call this, "continuations".
        if (newview.flowtoken == null) {
          if (prop.equals(ARIResult.FLOW_START) || prop.equals(ARIResult.FLOW_FASTSTART)) {
            // if the ARI wanted one and hasn't allocated one, allocate flow
            // token.
            newview.flowtoken = errorstatemanager.allocateToken();
//            informalflowmanager.startFlow(newview.flowtoken);
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
        newview.endflow = "1";
        if (viewparams.flowtoken != null) {
          presmanager.flowEnd(viewparams.flowtoken);
        }
      }
      // moved inside since this may itself cause an error!
      String submitting = PostDecoder.decodeSubmittingControl(normalizedmap);
      errorstatemanager.globaltargetid = submitting;
    }
    catch (Exception e) {
      Logger.log.error("Error invoking action", e);
      // ThreadErrorState.addError(new TargettedMessage(
      // CoreMessages.GENERAL_ACTION_ERROR));
      // Detect failure to fill out arires properly.
      if (ariresult == null || ariresult.resultingview == null
          || e instanceof IllegalStateException) {
        ariresult = new ARIResult();
        ariresult.propagatebeans = ARIResult.FLOW_END;

        ViewParameters defaultparameters = ves.handleException(e, viewparams);
        ariresult.resultingview = defaultparameters;
      }
    }

    String errortoken = errorstatemanager.requestComplete();

    ariresult.resultingview.errortoken = errortoken;
    return ariresult.resultingview;
  }

}