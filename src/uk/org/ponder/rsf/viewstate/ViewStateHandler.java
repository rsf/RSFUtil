/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.rsf.viewstate;

import java.util.Map;

/**
 * Provides primitive functionality for mapping view states to URLs, and issuing
 * redirects.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ViewStateHandler {
  /** Return a "complete" URL suitable for rendering to our upstream consumer for
   * a link to the view specified in these parameters. This URL may not be valid
   * for any external purposes. This form of URL is also to be used for issuing
   * redirects.
   */
  public String getFullURL(ViewParameters viewparams);

  /** Return a "complete" URL suitable for rendering to our upstream consumer for
   * triggering an "action" - in HTML, as applied to the "action" attribute of
   * <code>form</code>. This omits all state that might enter attributes,
   * which is supplied by getActionMap().
   */
  public String getActionURL(ViewParameters viewparams);

  /** Return a new, unshared map of all state from these ViewParameters that will
   * be mapped into attribute state.
   */
  public Map getAttrMap(ViewParameters viewparams);

  /** The equivalent of getFullURL for static resources that are not necessarily
   * under our control. These e.g. start with /content/...
   */
  public String getResourceURL(String resourcepath);

  /** Return a "fully resolved" complete URL linking to the view specified, which
   * is a valid URL resolvable on the internet at large.
   */
  public String getUltimateURL(ViewParameters viewparams);
}
