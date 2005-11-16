/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.errorutil.ConfigurationException;
import uk.org.ponder.errorutil.CoreMessages;
import uk.org.ponder.errorutil.ErrorUtil;
import uk.org.ponder.errorutil.PermissionException;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.renderer.ViewRender;
import uk.org.ponder.rsf.state.ErrorStateManager;
import uk.org.ponder.rsf.state.StatePreservationManager;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.view.ViewGenerator;
import uk.org.ponder.rsf.view.ViewProcessor;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.RunnableWrapper;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
// This has now become a request-scope bean.
public class GetHandler {
  // all request-scope dependencies
  private ViewGenerator viewgenerator;
  private ErrorStateManager errorstatemanager;
  private RunnableWrapper getwrapper;
  private ViewProcessor viewprocessor;
  private ViewParameters viewparams;
  
  private ErrorStateManager tokenrse;
  private StatePreservationManager presmanager;

  public void setViewGenerator(ViewGenerator viewgenerator) {
    this.viewgenerator = viewgenerator;
  }

  public void setErrorStateManager(ErrorStateManager errorstatemanager) {
    this.errorstatemanager = errorstatemanager;
  }

  public void setAlterationWrapper(RunnableWrapper getwrapper) {
    this.getwrapper = getwrapper;
  }

  public void setViewProcessor(ViewProcessor viewprocessor) {
    this.viewprocessor = viewprocessor;
  }
  
  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  public void setStatePreservationManager(StatePreservationManager presmanager) {
    this.presmanager = presmanager;
  }
  
  // Since this is a request-scope bean, there is no problem letting the
  // returned view from the getwrapper escape into this member.
  private View view;

  /** The beanlocator is passed in to allow the late location of the 
   * ViewRender bean which needs to occur in a controlled exception context.
   */
  public ViewParameters handle(PrintOutputStream pos, BeanLocator beanlocator) {
   
    boolean iserrorredirect = viewparams.errorredirect != null;
    // YES, reach into the original request! somewhat bad...
    viewparams.errorredirect = null;
    try {
      view = viewgenerator.getView();
//      final TokenRequestState trs = viewparams.errortoken == null ? 
//        null : requeststateentry.getTSHolder().getTokenState(viewparams.errortoken);
      getwrapper.wrapRunnable(new Runnable() {
        public void run() {
          if (viewparams.flowtoken != null) {
            presmanager.restore(viewparams.flowtoken);
          }
          // some slight failure of RSAC here. But this pipeline is a bit
          // clearer in code.
          viewprocessor.setView(view);
          view = viewprocessor.getProcessedView();
        }
      }).run();

      ViewRender viewrender = (ViewRender) beanlocator.locateBean("viewrender");
      if (errorstatemanager.errorstate != null) {
        viewrender.setMessages(errorstatemanager.errorstate.errors);
        viewrender.setGlobalMessageTarget(errorstatemanager.errorstate.globaltargetid);
      }
      viewrender.setView(view);
      viewrender.render(pos);
    }
    catch (Exception e) {
      // if a request comes in for an invalid view, redirect it onto a default
      ViewParameters redirect = handleLevel1Error(viewparams, e,
          iserrorredirect);
      return redirect;
    }
    finally {
      errorstatemanager.requestComplete();
    }
    // if an exception escapes this block, handler will externally call
    // renderFatalError
    return null;
  }

  // a "Level 1" GET error simply attempts to redirect onto a default
  // view, with errors intact.
  public ViewParameters handleLevel1Error(ViewParameters viewparams,
      Throwable t, boolean iserrorredirect) {
    ViewComponentProducer defaultview = viewgenerator.getViewCollection().getDefaultView();
    Logger.log.warn("Exception populating view root: ", t);
    UniversalRuntimeException invest = UniversalRuntimeException.accumulate(t);
    Throwable target = invest.getTargetException();
    if (target != null) {
      Logger.log.warn("Got target exception of " + target.getClass());
    }

    if (target instanceof ConfigurationException || target instanceof Error
        || target instanceof PermissionException || iserrorredirect) {
      throw invest;
    }

    String tokenid = viewparams.flowtoken;
    TargettedMessage newerror = 
      new TargettedMessage(CoreMessages.GENERAL_SHOW_ERROR,  new Object[] { tokenid });
    ThreadErrorState.addError(newerror);


    Logger.log.warn("Error creating view tree - token " + tokenid, t);

    ViewParameters defaultparameters = viewparams.copyBase();

    defaultview.fillDefaultParameters(defaultparameters);
    // make sure this method is prodded FIRST, because requestComplete 
    // (which would ordinarily allocate the token on seeing an error state) is
    // only called AFTER we visibly return from handle() above.
    defaultparameters.flowtoken = errorstatemanager.allocateOutgoingToken();
    defaultparameters.errorredirect = "1";
    return defaultparameters;
  }

  public void renderFatalError(Throwable t, PrintOutputStream pos) {
    // We may have such a fatal misconfiguration that we can't even rely on
    // IKAT to format this error message
    Logger.log.fatal("Completely fatal error populating view root", t);

    pos.println("<html><head><title>Internal Error</title></head></body><pre>");
    pos.println("Fatal internal error handling request: " + t);
    ErrorUtil.dumpStackTrace(t, pos);
    pos.println("</pre></body></html>");
    pos.close();
  }

}