/*
 * Created on 25 Mar 2008
 */
package uk.org.ponder.rsf.processor.support;

import uk.org.ponder.rsf.preservation.StatePreservationManager;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.RunnableInvoker;

/** Collects and focusses standard RSF request bracketing into a single
 * RunnableInvoker. Combines the effect of the alteration wrapper together
 * with relevant scope restoration.
 * 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class RequestInvoker implements RunnableInvoker {

  private RunnableInvoker alterationwrapper;
  private StatePreservationManager presmanager;
  private ViewParameters viewparamsproxy;

  public void setAlterationWrapper(RunnableInvoker alterationwrapper) {
    this.alterationwrapper = alterationwrapper;
  }
  
  public void setStatePreservationManager(StatePreservationManager presmanager) {
    this.presmanager = presmanager;
  }
  
  public void setViewParametersProxy(ViewParameters viewparamsproxy) {
    this.viewparamsproxy = viewparamsproxy;
  }
  
  public void invokeRunnable(final Runnable torun) {
    // *outside* alteration wrapper so that AW may be BeanFetchBracketed.
    presmanager.scopeRestore();
    alterationwrapper.invokeRunnable(new Runnable() {

      public void run() {
        ViewParameters viewparams = (ViewParameters) viewparamsproxy.get();
        if (viewparams.flowtoken != null) {
          presmanager.restore(viewparams.flowtoken, viewparams.endflow != null);
        }
        torun.run();
        presmanager.scopePreserve();
      }});
  }

}
