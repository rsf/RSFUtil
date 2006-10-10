/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.view;

/**
 * An interface which may be used during component production, to avoid
 * generating (expensive) components which will not be rendered by the
 * current template.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ComponentChecker {
/**
 * Returns <code>true</code> if the template set for the current view contains
 * any demand for a component with the supplied ID.
 */
  public boolean hasComponent(String ID);
}
