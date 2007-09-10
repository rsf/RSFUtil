/*
 * Created on 10 Sep 2007
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

/** Interface to the "core", reflective ViewParamsCodec, that directly applies
 * the information determined as {@link ViewParamsMapInfo} entries.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface CoreViewParamsCodec extends ViewParamsCodec {

  /** Render the supplied ViewParams into an attribute-only representation, with
   * former "trunk paths" supplied with the specified priority.
   */
  public Map renderViewParamsNonTrunk(ViewParameters toparse, boolean highpriority);
  
}
