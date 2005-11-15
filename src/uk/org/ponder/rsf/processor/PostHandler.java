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
import uk.org.ponder.rsf.state.RequestStateEntry;
import uk.org.ponder.rsf.state.RequestSubmittedValueCache;
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
  private PostDecoder postdecoder;

  // request-scope dependencies
  private Map normalizedmap;
  private ViewParameters viewparams;
  private RequestStateEntry requeststateentry;
  private RSVCApplier rsvcapplier;

  public void setRequestState(RequestStateEntry requeststateentry) {
    this.requeststateentry = requeststateentry;
  }

  public void setActionResultInterpreter(ActionResultInterpreter ari) {
    this.ari = ari;
  }

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  public void setPostDecoder(PostDecoder postdecoder) {
    this.postdecoder = postdecoder;
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

  // Since this entire bean is request scope, there is no difficulty with
  // letting
  // the action result escape from the wrapper into this instance variable.
  private String actionresult = null;

  public ViewParameters handle() {
  
    final String actionmethod = PostDecoder.decodeAction(normalizedmap);

    final RequestSubmittedValueCache basersvc;
    if (viewparams.viewtoken != null) {
      basersvc = requeststateentry.getTSHolder().getTokenState(
          viewparams.viewtoken).rsvc;
      // TODO: Think very carefully whether we want to fork the rsvc here.
      // Again this is an application policy - rsvc PER FLOW, or rsvc PER FORK?
      // Fork would result in rather tricky semantics that not all might want.
    }
    else {
      basersvc = new RequestSubmittedValueCache();
    }

    try {
      // invoke all state-altering operations within the runnable wrapper.
      postwrapper.wrapRunnable(new Runnable() {
        public void run() {
          postdecoder.accreteRSVC(normalizedmap, basersvc);
          rsvcapplier.applyValues(basersvc);

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
    requeststateentry.globaltargetid = submitting;

    ARIResult arires = ari.interpretActionResult(viewparams, actionresult);
    requeststateentry.requestComplete(basersvc, arires.propagatebeans);
    arires.resultingview.viewtoken = requeststateentry.getOutgoingToken();
    return arires.resultingview;
  }

}