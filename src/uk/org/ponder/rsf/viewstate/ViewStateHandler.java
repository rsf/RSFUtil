/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.rsf.viewstate;

/**
 * Provides primitive functionality for mapping view states to URLs, and
 * issuing redirects.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ViewStateHandler { 
  /** Return a "complete" URL suitable for rendering to our upstream 
   * consumer for a link to the view specified in these parameters.
   * This URL may not be valid for any external purposes. This form of
   * URL is also to be used for issuing redirects.
   */
  public String getFullURL(ViewParameters viewparams);
  /** The equivalent of getFullURL for static resources that are not
   * necessarily under our control. These e.g. start with /content/...
   */
  public String getResourceURL(String resourcepath);
  /** Return a "fully resolved" complete URL linking to the view
   * specified, which is a valid URL resolvable on the internet at
   * large. 
   */
  public String getUltimateURL(ViewParameters viewparams);
}
