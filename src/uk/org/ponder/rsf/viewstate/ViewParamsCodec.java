/*
 * Created on 16 Jul 2007
 */
package uk.org.ponder.rsf.viewstate;

/** Converts ViewParameters objects to and from a "broken-down" raw
 * representation.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface ViewParamsCodec {
  /** A rendering of the supplied ViewParameters into a Raw form, or <code>null</code>
   * if the parameters are not supported.
   */
  public RawURLState renderViewParams(ViewParameters toparse);
  public boolean parseViewParams(ViewParameters target, RawURLState rawstate);
}
