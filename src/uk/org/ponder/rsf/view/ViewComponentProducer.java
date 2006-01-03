/*
 * Created on Aug 11, 2005
 */
package uk.org.ponder.rsf.view;

/** The "main" generator of view components for a view. In addition to 
 * responsibilities as a plain ComponentProducer, 
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface ViewComponentProducer extends ComponentProducer {
  public String getViewID();
}
