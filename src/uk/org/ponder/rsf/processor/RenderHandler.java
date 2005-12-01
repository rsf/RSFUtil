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
import uk.org.ponder.rsf.flow.ViewExceptionStrategy;
import uk.org.ponder.rsf.state.ErrorStateManager;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * This request-scope bean exists to bracket the creation and operation of the 
 * rendering process, such that any "first" exception causes a redirect to a default
 * view. A "double fault", caught above in the RootHandlerBean, 
 * will render a fatal error message in the raw.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RenderHandler {
  private ViewExceptionStrategy ves;
  private ErrorStateManager errorstatemanager;
  private ViewParameters viewparams;

  public void setViewExceptionStrategy(ViewExceptionStrategy ves) {
    this.ves = ves;
  }
  
  public void setErrorStateManager(ErrorStateManager errorstatemanager) {
    this.errorstatemanager = errorstatemanager;
  }
  
  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  /** The beanlocator is passed in to allow the late location of the 
   * GetHandlerImpl bean which needs to occur in a controlled exception context.
   */
  public ViewParameters handle(PrintOutputStream pos, BeanLocator beanlocator) {
   
    boolean iserrorredirect = viewparams.errorredirect != null;
    // YES, reach into the original request! somewhat bad...
    viewparams.errorredirect = null;
    try {
      RenderHandlerImpl gethandlerimpl = (RenderHandlerImpl) beanlocator.locateBean("gethandlerimpl");
      gethandlerimpl.handle(pos);
    
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
      Exception e, boolean iserrorredirect) {
    Logger.log.warn("Exception rendering view: ", e);
    UniversalRuntimeException invest = UniversalRuntimeException.accumulate(e);
    Throwable target = invest.getTargetException();
    if (target != null) {
      Logger.log.warn("Got target exception of " + target.getClass());
    }

    if (target instanceof ConfigurationException || target instanceof Error
        || target instanceof PermissionException || iserrorredirect) {
      throw invest;
    }

    ViewParameters defaultparameters = ves.handleException(e, viewparams);
    
    // make sure this method is prodded FIRST, because requestComplete 
    // (which would ordinarily allocate the token on seeing an error state) is
    // only called AFTER we visibly return from handle() above.
    String tokenid = errorstatemanager.allocateOutgoingToken();
    defaultparameters.errortoken = tokenid;
    defaultparameters.errorredirect = "1";

    Logger.log.warn("Error creating view tree - token " + tokenid);
    
    TargettedMessage newerror = 
      new TargettedMessage(CoreMessages.GENERAL_SHOW_ERROR,  new Object[] { tokenid });
    ThreadErrorState.addError(newerror);
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