/*
 * Created on 11 Mar 2007
 */
package uk.org.ponder.rsf.evolvers;

/** A refinement of the {@link DynamicListInputEvolver} that allows restrictions
 * to be placed on the number of items that can be input.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public interface BoundedDynamicListInputEvolver extends DynamicListInputEvolver {
  /** Set the minimum number of list elements that the interface will permit
   * to be displayed */
  public void setMinimumLength(int minlength);

  /** Set the maximum number of list elements that the interface will permit
   * to be displayed */
  public void setMaximumLength(int maxlength);

}
