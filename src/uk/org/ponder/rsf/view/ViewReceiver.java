/*
 * Created on Dec 3, 2005
 */
package uk.org.ponder.rsf.view;
/** This interface is used to break load-order race between ComponentProcessors
 * and RenderHandlerImpl.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface ViewReceiver {
  public void setView(View view);
}
