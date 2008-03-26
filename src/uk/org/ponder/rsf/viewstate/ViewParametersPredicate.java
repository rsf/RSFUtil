/*
 * Created on 26 Mar 2008
 */
package uk.org.ponder.rsf.viewstate;

/** An interface specifying a rule which conditionally accepts a ViewParameters
 * instance.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ViewParametersPredicate {
  public boolean accept(ViewParameters tocheck);
}
