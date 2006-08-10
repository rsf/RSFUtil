/*
 * Created on 10 Aug 2006
 */
package uk.org.ponder.rsf.viewstate;

/** Allows a prefix to be specified for UIInternalLink components which
 * are *not* participating in the ViewParameters system, but have 
 * specified a raw link. In general the empty string.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface InternalBaseURLProvider {
  public String getInternalBaseURL();
}
