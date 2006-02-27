/*
 * Created on Aug 11, 2005
 */
package uk.org.ponder.rsf.view;

/** The "main" generator of view components for a view. A ViewComponentProducer
 * serves a single view, that it, it corresponds to a single root view template.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface ViewComponentProducer extends ComponentProducer {
  public String getViewID();
}
