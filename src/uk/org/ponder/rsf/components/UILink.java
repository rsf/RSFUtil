/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * This component peers with a simple navigational link. Operating this link
 * is expected to give rise to an idempotent request.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class UILink extends UIOutput {
  /** A string representing the target of this link - e.g. in an HTML system,
   * a URL.
   */
  public String target;
}
