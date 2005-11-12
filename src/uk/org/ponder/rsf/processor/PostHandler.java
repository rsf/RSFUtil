/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.processor;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.errorutil.CoreMessages;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
import uk.org.ponder.rsf.renderer.RenderSystem;
import uk.org.ponder.rsf.state.RSVCApplier;
import uk.org.ponder.rsf.state.RequestStateEntry;
import uk.org.ponder.rsf.state.RequestSubmittedValueCache;
import uk.org.ponder.rsf.state.SubmittedValueEntry;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.RunnableWrapper;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk) PostHandler is a request
 *         scope bean responsible for handling an HTTP POST request.
 */
public class PostHandler {
  // application-scope dependencies
  private ActionResultInterpreter ari;
  private RunnableWrapper postwrapper;
  private PostDecoder postdecoder;

  // request-scope dependencies
  private RenderSystem rendersystem; // should be split. No rendering done on
                                      // this path.
  private ViewParameters viewparams;
  private RequestStateEntry requeststateentry;
  private RSVCApplier rsvcapplier;

  public void setRequestState(RequestStateEntry requeststateentry) {
    this.requeststateentry = requeststateentry;
  }

  public void setActionResultInterpreter(ActionResultInterpreter ari) {
    this.ari = ari;
  }

  public void setRenderSystem(RenderSystem rendersystem) {
    this.rendersystem = rendersystem;
  }

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  public void setPostDecoder(PostDecoder postdecoder) {
    this.postdecoder = postdecoder;
  }

  public void setPostAlterationWrapper(RunnableWrapper postwrapper) {
    this.postwrapper = postwrapper;
  }

  public void setRSVCApplier(RSVCApplier rsvcapplier) {
    this.rsvcapplier = rsvcapplier;
  }

  // Since this entire bean is request scope, there is no difficulty with
  // letting
  // the action result escape from the wrapper into this instance variable.
  private String actionresult = null;

  public ViewParameters handle(Map origrequestparams) {
    final HashMap requestparams = new HashMap();
    requestparams.putAll(origrequestparams);
    rendersystem.normalizeRequestMap(requestparams);

    final String actionmethod = PostDecoder.decodeAction(requestparams);

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
          postdecoder.accreteRSVC(requestparams, basersvc);
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

    String linkid = TargettedMessage.TARGET_NONE;

    ARIResult arires = ari.interpretActionResult(viewparams, actionresult);
    requeststateentry.requestComplete(basersvc);

    arires.resultingview.viewtoken = requeststateentry.outgoingtokenID;
    return arires.resultingview;
  }

}