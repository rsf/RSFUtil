/*
 * Created on 16 Jul 2007
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

import uk.org.ponder.rsf.viewstate.support.ViewParamsMapper;

/** Converts ViewParameters objects to and from a "broken-down" raw
 * representation. The "default" implementation is the {@link ViewParamsMapper}.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface ViewParamsCodec {
  /** Returns <code>true</code> if this type (or instance) of ViewParameters is supported
   * by this codec.
   */
  public boolean isSupported(ViewParameters viewparams);
  /** A rendering of the supplied ViewParameters into a Raw form, or <code>null</code>
   * if the parameters are not supported.
   */
  public RawURLState renderViewParams(ViewParameters toparse);
  public boolean parseViewParams(ViewParameters target, RawURLState rawstate, Map unusedParams);
}
