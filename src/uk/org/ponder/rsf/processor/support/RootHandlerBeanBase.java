/*
 * Created on 9 Oct 2007
 */
package uk.org.ponder.rsf.processor.support;

import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.content.ContentTypeInfo;
import uk.org.ponder.rsf.processor.ActionHandler;
import uk.org.ponder.rsf.processor.FatalErrorHandler;
import uk.org.ponder.rsf.processor.HandlerHook;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.request.LazarusRedirector;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;
import uk.org.ponder.streamutil.write.PrintOutputStream;

/** Abstracts the common functionality of RootHandlerBeans across the different
 * environments - operating the overall request cycle, distinguishing between
 * render and action cycles.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public abstract class RootHandlerBeanBase {
  private FatalErrorHandler fatalErrorHandler;
  private RenderHandlerBracketer renderhandlerbracketer;
  private String requesttype;
  private HandlerHook handlerhook;
  private ActionHandler actionhandler;
  protected ContentTypeInfo contenttypeinfo;
  protected LazarusRedirector lazarusRedirector;
  protected ParameterList outgoingparams;
  protected ViewStateHandler viewstatehandler;

  public void setRenderHandlerBracketer(
      RenderHandlerBracketer renderhandlerbracketer) {
    this.renderhandlerbracketer = renderhandlerbracketer;
  }
  
  public void setActionHandler(ActionHandler actionhandler) {
    this.actionhandler = actionhandler;
  }
  
  public void setRequestType(String requesttype) {
    this.requesttype = requesttype;
  }

  public void setFatalErrorHandler(FatalErrorHandler fatalErrorHandler) {
    this.fatalErrorHandler = fatalErrorHandler;
  }
  
  public void setHandlerHook(HandlerHook handlerhook) {
    this.handlerhook = handlerhook;
  }

  public void setContentTypeInfo(ContentTypeInfo contenttypeinfo) {
    this.contenttypeinfo = contenttypeinfo;
  }
  
  public void setLazarusRedirector(LazarusRedirector lazarusRedirector) {
    this.lazarusRedirector = lazarusRedirector;
  }
  
  public void setOutgoingParams(ParameterList outgoingparams) {
    this.outgoingparams = outgoingparams;
  }
  
  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }
  
  public boolean handle() {
    if (handlerhook == null || !handlerhook.handle()) {
      if (requesttype.equals(EarlyRequestParser.RENDER_REQUEST)) {
        handleGet();
      }
      else {
        handlePost();
      }
    }
    return true;
  }
  
  private void handleGet() {
    PrintOutputStream pos = setupResponseWriter();
    AnyViewParameters redirect = null;
    try {
      redirect = renderhandlerbracketer.handle(pos);

      if (redirect != null) {
        issueRedirect(redirect, pos);
      }
    }
    catch (Throwable t) {
      DefaultFatalErrorHandler.handleFatalErrorStrategy(fatalErrorHandler, t,
          pos);
    }
    finally {
      if (redirect == null) {
        pos.close();
      }
    }
  }

  private void handlePost() {

    AnyViewParameters redirect = actionhandler.handle();

    issueRedirect(redirect, null);
  }

  public abstract PrintOutputStream setupResponseWriter();
  
  public abstract void issueRedirect(AnyViewParameters viewparamso, PrintOutputStream pos);
  
}
