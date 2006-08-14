/*
 * Created on 14 Aug 2006
 */
package uk.org.ponder.rsf.viewstate;

/** Returns the external URL corresponding to the context resource base for
 * this application.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface ContextURLProvider {
  public String getContextBaseURL();
}
