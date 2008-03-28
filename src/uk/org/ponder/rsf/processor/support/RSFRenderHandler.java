/*
 * Created on Nov 20, 2005
 */
package uk.org.ponder.rsf.processor.support;

import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.componentprocessor.ViewProcessor;
import uk.org.ponder.rsf.processor.RenderHandler;
import uk.org.ponder.rsf.renderer.ViewRender;
import uk.org.ponder.rsf.state.support.ErrorStateManager;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.support.ViewGenerator;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.RunnableInvoker;

/**
 * Controls the operation of a "render cycle" of RSF. Locates component
 * producers, generates the view, performs fixups, and invokes the renderer. Any
 * errors through the construction of this bean are passed back through GetHandler.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RSFRenderHandler implements RenderHandler {
  // all request-scope dependencies
  private ViewGenerator viewgenerator;
  private ErrorStateManager errorstatemanager;
  private ViewProcessor viewprocessor;
  private ViewParameters viewparams;

  private RunnableInvoker requestInvoker;
  private ViewRender viewrender;
  private TargettedMessageList targettedMessageList;
  private boolean enableDebugRendering;

  public void setTargettedMessageList(TargettedMessageList targettedMessageList) {
    this.targettedMessageList = targettedMessageList;
  }

  public void setViewGenerator(ViewGenerator viewgenerator) {
    this.viewgenerator = viewgenerator;
  }

  public void setErrorStateManager(ErrorStateManager errorstatemanager) {
    this.errorstatemanager = errorstatemanager;
  }

  public void setRequestInvoker(RunnableInvoker requestInvoker) {
    this.requestInvoker = requestInvoker;
  }

  public void setViewProcessor(ViewProcessor viewprocessor) {
    this.viewprocessor = viewprocessor;
  }

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  public void setEnableDebugRendering(boolean enableDebugRendering) {
    this.enableDebugRendering = enableDebugRendering;
  }
  
  public void setViewRender(ViewRender viewrender) {
    this.viewrender = viewrender;
  }

  // Since this is a request-scope bean, there is no problem letting the
  // returned view from the getwrapper escape into this member.
  private View view;


  public void handle(PrintOutputStream pos) {
    requestInvoker.invokeRunnable(new Runnable() {
      public void run() {
        // this must now be AFTER restoration since the templateexpander may
        // access the model. Shucks!!
        view = viewgenerator.generateView();
        viewprocessor.setView(view);
        view = viewprocessor.getProcessedView();
      }
    });
    viewrender.setMessages(targettedMessageList);
    // TODO: globaltargetid detection has not been investigated for a while
    viewrender.setGlobalMessageTarget(errorstatemanager.errorstate.globaltargetid);
    viewrender.setView(view);
    viewrender.setDebugRender(enableDebugRendering && viewparams.debugrender != null);
    viewrender.render(pos);
  }

}
