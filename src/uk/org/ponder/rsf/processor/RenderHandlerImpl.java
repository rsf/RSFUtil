/*
 * Created on Nov 20, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.rsf.renderer.ViewRender;
import uk.org.ponder.rsf.state.ErrorStateManager;
import uk.org.ponder.rsf.state.StatePreservationManager;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.ViewGenerator;
import uk.org.ponder.rsf.view.ViewProcessor;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.RunnableWrapper;

/**
 * Controls the operation of a "render cycle" of RSF. Locates component
 * producers, generates the view, performs fixups, and invokes the renderer. Any
 * errors through the construction of this bean are passed back through GetHandler.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RenderHandlerImpl {
  // all request-scope dependencies
  private ViewGenerator viewgenerator;
  private ErrorStateManager errorstatemanager;
  private RunnableWrapper getwrapper;
  private ViewProcessor viewprocessor;
  private ViewParameters viewparams;

  private StatePreservationManager presmanager;
  private ViewRender viewrender;

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

  public void setViewRender(ViewRender viewrender) {
    this.viewrender = viewrender;
  }

  // Since this is a request-scope bean, there is no problem letting the
  // returned view from the getwrapper escape into this member.
  private View view;

  /**
   * The beanlocator is passed in to allow the late location of the ViewRender
   * bean which needs to occur in a controlled exception context.
   */
  public void handle(PrintOutputStream pos) {
    view = viewgenerator.getView();
    getwrapper.wrapRunnable(new Runnable() {
      public void run() {
        if (viewparams.flowtoken != null) {
          presmanager.restore(viewparams.flowtoken, viewparams.endflow != null);
        }
        viewprocessor.setView(view);
        view = viewprocessor.getProcessedView();
      }
    }).run();

    if (errorstatemanager.errorstate != null) {
      viewrender.setMessages(errorstatemanager.errorstate.errors);
      viewrender
          .setGlobalMessageTarget(errorstatemanager.errorstate.globaltargetid);
    }
    viewrender.setView(view);
    viewrender.render(pos);
  }

}
