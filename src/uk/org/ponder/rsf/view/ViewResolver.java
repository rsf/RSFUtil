/*
 * Created on Nov 25, 2005
 */
package uk.org.ponder.rsf.view;

import java.util.List;

/** The core interface locating the set of view (component) producers for 
 * a given viewID.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */
public interface ViewResolver {
  public List getProducers(String viewid);
}
