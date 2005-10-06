/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.beanutil.BeanGetter;
import uk.org.ponder.errorutil.ConfigurationException;
import uk.org.ponder.errorutil.CoreMessages;
import uk.org.ponder.errorutil.ErrorUtil;
import uk.org.ponder.errorutil.PermissionException;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.rsf.renderer.ViewRender;
import uk.org.ponder.rsf.util.TokenStateHolder;
import uk.org.ponder.rsf.util.ViewCollection;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.webapputil.ViewParameters;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class GetHandler {
  // This is a hash of String viewIDs to Views.
  private ViewCollection viewcollection;

  public void setViewCollection(ViewCollection viewcollection) {
    this.viewcollection = viewcollection;
  }

  public ViewCollection getViewCollection() {
    return viewcollection;
  }


  private TokenStateHolder tsholder;

  public void setTSHolder(TokenStateHolder errorhandler) {
    this.tsholder = errorhandler;
  }

  public ViewParameters handle(PrintOutputStream pos, BeanGetter beangetter) {
    ViewParameters viewparams = (ViewParameters) beangetter.getBean("viewparameters");
    boolean iserrorredirect = viewparams.errorredirect != null;
    // YES, reach into the original request! somewhat bad...
    viewparams.errorredirect = null;
    try {
      ViewRender viewrender = (ViewRender) beangetter.getBean("viewrender");
      viewrender.render(pos);
    }
    catch (Exception e) {
      // if a request comes in for an invalid view, redirect it onto a default
      ViewParameters redirect = handleLevel1Error(viewparams, e, iserrorredirect);
      return redirect;
    }
    finally {
      tsholder.errorAccumulationComplete(null);
    }
    return null;
  }


  // a "Level 1" GET error simply attempts to redirect onto a default
  // view, with errors intact.
  public ViewParameters handleLevel1Error(ViewParameters viewparams,
      Throwable t, boolean iserrorredirect) {
    ViewComponentProducer defaultview = viewcollection.getDefaultView();
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

    TargettedMessage newerror = new TargettedMessage(null);
    ThreadErrorState.addError(newerror);

    String tokenid = viewparams.viewtoken;
    String generalerror = defaultview.getMessageLocator().getMessage(
        CoreMessages.GENERAL_SHOW_ERROR, new Object[] { tokenid });
    newerror.message = generalerror;
    Logger.log.warn("Error creating view tree - token " + tokenid, t);

    ViewParameters defaultparameters = (ViewParameters) viewparams.copyBase();

    defaultview.fillDefaultParameters(defaultparameters);

    defaultparameters.viewtoken = ThreadErrorState.getErrorState().outgoingtokenID;
    defaultparameters.errorredirect = "1";
    return defaultparameters;
  }

  public void renderFatalError(Throwable t, PrintOutputStream pos) {
    Logger.log.fatal("Completely fatal error populating view root", t);

    pos.println("<html><head><title>Internal Error</title></head></body><pre>");
    pos.println("Fatal internal error handling request: " + t);
    ErrorUtil.dumpStackTrace(t, pos);
    pos.println("</pre></body></html>");
    pos.close();
  }

}