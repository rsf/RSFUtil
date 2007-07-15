/*
 * Created on 18 May 2007
 */
package uk.org.ponder.rsf.viewstate;

/** An interception interface that allows implementors to adjust the
 * state of the inferred ViewParameters before they are parsed into the
 * request.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface ViewParamsInterceptor {
  public AnyViewParameters adjustViewParameters(ViewParameters incoming);
}
