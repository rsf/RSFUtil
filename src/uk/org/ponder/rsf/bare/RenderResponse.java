/*
 * Created on 6 Feb 2008
 */
package uk.org.ponder.rsf.bare;

import uk.org.ponder.rsf.viewstate.AnyViewParameters;

/** Represents the output from an RSF render cycle **/

public class RenderResponse extends RequestResponse {
  /** A ViewWrapper object wrapping the returned component tree, if any **/
  public ViewWrapper viewWrapper;
  /** A string holding the rendered markup, if any **/
  public String markup;
  /** If the request concluded in error or with a client redirect for another
   * reason, the target of the redirect, else <code>null</code>.
   */
  public AnyViewParameters redirect;
}
