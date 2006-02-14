/*
 * Created on 14-Feb-2006
 */
package uk.org.ponder.rsf.components;

/** An interface to be called during the fixup stage, once the component
 * has reached its proper place in the tree. Typically used to infer 
 * default values for any fields that have still not been set, and set IDs
 * on parts of composite components.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface FixableComponent {
  public void fixupComponent();
}
