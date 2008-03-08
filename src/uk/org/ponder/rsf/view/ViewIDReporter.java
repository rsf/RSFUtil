/*
 * Created on 7 Mar 2008
 */
package uk.org.ponder.rsf.view;

/** Represents a bean that is associated with a particular View ID - either a 
 * ViewComponentProducer or a {@link DataView}.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * @since 0.7.3
 */

public interface ViewIDReporter {
  /** Returns the viewID for this view, which may be used to look up a 
   * ViewParameters class **/
  public String getViewID();
}
