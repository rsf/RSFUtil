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
import uk.org.ponder.rsf.renderer.ViewRender;
import uk.org.ponder.rsf.state.RSVCApplier;
import uk.org.ponder.rsf.state.RequestStateEntry;
import uk.org.ponder.rsf.state.RequestSubmittedValueCache;
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
  private RequestStateEntry requeststateentry;
  private RunnableWrapper getwrapper;
  private ViewProcessor viewprocessor;
  private RSVCApplier rsvcapplier;
  private ViewParameters viewparams;

  public void setRSVCApplier(RSVCApplier rsvcapplier) {
    this.rsvcapplier = rsvcapplier;
  }

  public void setViewGenerator(ViewGenerator viewgenerator) {
    this.viewgenerator = viewgenerator;
  }

  public void setRequestState(RequestStateEntry requeststateentry) {
    this.requeststateentry = requeststateentry;
  }

  public void setGetAlterationWrapper(RunnableWrapper getwrapper) {
    this.getwrapper = getwrapper;
  }

  public void setViewProcessor(ViewProcessor viewprocessor) {
    this.viewprocessor = viewprocessor;
  }
  
  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  // Since this is a request-scope bean, there is no problem letting the
  // returned
  // view from the getwrapper escape into this member.
  private View view;

  public ViewParameters handle(PrintOutputStream pos, BeanLocator beanlocator) {
   
    boolean iserrorredirect = viewparams.errorredirect != null;
    // YES, reach into the original request! somewhat bad...
    viewparams.errorredirect = null;
    try {
      view = viewgenerator.getView();
      getwrapper.wrapRunnable(new Runnable() {
        public void run() {

          if (viewparams.viewtoken != null) {
            // TODO: generalise this to allow bean-transfer, and fuse with POST processing.
            RequestSubmittedValueCache rsvc = requeststateentry.getTSHolder()
                .getTokenState(viewparams.viewtoken).rsvc;
            rsvcapplier.applyValues(rsvc);
          }
          // some slight failure of RSAC here. But this pipeline is a bit
          // clearer in code.
          viewprocessor.setView(view);
          view = viewprocessor.getView();
        }
      }).run();

      ViewRender viewrender = (ViewRender) beanlocator.locateBean("viewrender");
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
      requeststateentry.requestComplete(null);
    }
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

    TargettedMessage newerror = new TargettedMessage(null);
    ThreadErrorState.addError(newerror);

    String tokenid = viewparams.viewtoken;
    String generalerror = defaultview.getMessageLocator().getMessage(
        CoreMessages.GENERAL_SHOW_ERROR, new Object[] { tokenid });
    newerror.message = generalerror;
    Logger.log.warn("Error creating view tree - token " + tokenid, t);

    ViewParameters defaultparameters = viewparams.copyBase();

    defaultview.fillDefaultParameters(defaultparameters);

    defaultparameters.viewtoken = requeststateentry.outgoingtokenID;
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