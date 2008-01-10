/*
 * Created on 19 Jul 2007
 */
package uk.org.ponder.rsf.viewstate;

/** Allows a client to look up the registered view parameters "exemplar" for
 * a particular ViewID.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ViewParamsRegistry {
  /** Return a fresh clone of the exemplar for the specified view - this will
   * be of the correct type, perhaps with some initialised fields, but an 
   * unshared copy ready to be fully initialised, perhaps with a call to 
   * {@link ViewParamsCodec#parseViewParams(ViewParameters, RawURLState)}.
   * The <code>viewID</code> field in the returned exemplar will be set to the
   * argument supplied.
   */
  public ViewParameters getViewParamsExemplar(String viewID);

}