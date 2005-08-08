/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.view;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ComponentChecker {
  // used during component generation to avoid generating unrendered
  // components.
  public boolean hasComponent(String ID);
}
