/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.processor;

import java.util.List;

import uk.org.ponder.errorutil.ConfigurationException;
import uk.org.ponder.errorutil.CoreMessages;
import uk.org.ponder.errorutil.ErrorUtil;
import uk.org.ponder.errorutil.PermissionException;
import uk.org.ponder.errorutil.RequestSubmittedValueCache;
import uk.org.ponder.errorutil.SubmittedValueEntry;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.errorutil.TokenRequestState;
import uk.org.ponder.rsf.components.BasicComponentSetter;
import uk.org.ponder.rsf.components.ComponentProcessor;
import uk.org.ponder.rsf.components.ComponentSetter;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.html.BasicHTMLComponentRenderer;
import uk.org.ponder.rsf.html.ComponentRenderer;
import uk.org.ponder.rsf.html.ViewRender;
import uk.org.ponder.rsf.util.ComponentDumper;
import uk.org.ponder.rsf.util.ComponentProducer;
import uk.org.ponder.rsf.util.CoreRSFMessages;
import uk.org.ponder.rsf.util.TokenStateHolder;
import uk.org.ponder.rsf.util.ViewCollection;
import uk.org.ponder.rsf.util.ViewComponentProducer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.TemplateResolver;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.ViewTemplate;
import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.streamutil.PrintStreamPOS;
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

  private TemplateResolver templateresolver;

  // Springise this when we have more than 1
  private ComponentRenderer renderer = new BasicHTMLComponentRenderer();

  private TokenStateHolder tsholder;

  public void setTSHolder(TokenStateHolder errorhandler) {
    this.tsholder = errorhandler;
  }

  public void setViewCollection(ViewCollection viewcollection) {
    this.viewcollection = viewcollection;
  }

  public ViewCollection getViewCollection() {
    return viewcollection;
  }

  public void setTemplateResolver(TemplateResolver templateresolver) {
    this.templateresolver = templateresolver;
  }

  public void setComponentRenderer(ComponentRenderer renderer) {
    this.renderer = renderer;
  }

  private ComponentSetter setter = new BasicComponentSetter();

  public ViewParameters handle(ViewParameters request, PrintOutputStream pos) {
    boolean iserrorredirect = request.errorredirect != null;
    request.errorredirect = null;
    try {
      ViewTemplate template = templateresolver.locateTemplate(request);
      View view = generateView(request, template);

      PrintOutputStream dumppos = new PrintStreamPOS(System.out);
      ComponentDumper.dumpContainer(view.viewroot, 0, dumppos);

      ComponentProcessor rsvcfixer = getRSVCFixer(view, request);
      ViewRender viewrender = template.getViewRender();
      // Fork a renderer for this request, which is aware of the relative path
      // of the template file which it will be fed.
      ComponentRenderer reqrenderer = renderer.copy();
      reqrenderer.setRelativeTemplatePath(template.getRelativePath());

      viewrender.init(view, reqrenderer, pos, rsvcfixer);

      viewrender.render();

    }
    catch (Exception e) {
      // if a request comes in for an invalid view, redirect it onto a default
      ViewParameters redirect = handleLevel1Error(request, e, iserrorredirect);
      return redirect;
    }
    finally {
      tsholder.errorAccumulationComplete(null);
    }
    return null;
  }

  // Returns a ComponentProcessor which will do the work of setting the
  // value of a component to the value obtained from the RSVC stored at the
  // CURRENT view token.
  public ComponentProcessor getRSVCFixer(View tofix, ViewParameters origrequest) {
    if (origrequest.viewtoken != null) {
      TokenRequestState cachedtrs = tsholder
          .getTokenState(origrequest.viewtoken);
      if (cachedtrs == null) {
        Logger.log
            .info("INTERESTING EVENT!! User requested error state which has expired from the cache");

        ThreadErrorState.getErrorState().errors
            .addMessage(new TargettedMessage(TargettedMessage.TARGET_NONE,
                CoreRSFMessages.EXPIRED_TOKEN));
      }
      else {
        final RequestSubmittedValueCache rsvc = cachedtrs.rsvc;
        if (rsvc != null) {
          return new ComponentProcessor() {

            public void processComponent(UIComponent toprocess) {
              SubmittedValueEntry sve = rsvc.byID(toprocess.getFullID());
              if (sve != null) {
                setter.setValue(toprocess, sve.oldvalue);
              }
            }

          };
        }
      }
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

    ViewParameters defaultparameters = viewparams.copyBase();

    defaultview.fillDefaultParameters(defaultparameters);

    defaultparameters.viewtoken = ThreadErrorState.getErrorState().outgoingtokenID;
    defaultparameters.errorredirect = "1";
    return defaultparameters;
  }

  /**
   * Returns the UIViewRoot for the view created by the View instance matching
   * the view ID. Any potentially recoverable errors are caught and a redirect
   * is issued onto a default page. If the error appears unrecoverable, simply
   * render an error page.
   */
  public View generateView(ViewParameters viewparams, ComponentChecker checker) {
    View view = new View();
    List producers = viewcollection.getProducers(viewparams.viewID);

    if (producers != null) {
      for (int i = 0; i < producers.size(); ++i) {
        ComponentProducer producer = (ComponentProducer) producers.get(i);

        producer.fillComponents(view.viewroot, viewparams, checker);
      }
      return view;
    }
    else {
      throw new UniversalRuntimeException(
          "Request intercepted for unknown view " + viewparams.viewID);
    }
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