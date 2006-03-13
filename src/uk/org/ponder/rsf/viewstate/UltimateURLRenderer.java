/*
 * Created on 12-Mar-2006
 */
package uk.org.ponder.rsf.viewstate;

/** Renders "ultimate" URLs, that is, URLs for global navigation of an entire
 * consumer portal to show a given resource, which may differ from the specific
 * URL responsible for rendering the current request. This is broken out from
 * ViewStateHandler to be able to incorporate possibly complex consumer-specific
 * logic. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface UltimateURLRenderer {
  public String getConsumerType();
  /** @param consumerultimatebaseURL The URL entered in the ConsumerInfo structure
   * for this request under <code>externalURL</code>.
   * @param viewparams The view parameters for this request
   * @param vsh The ViewStateHandler active for this request - this in general
   * will be a callback to the caller, since the required URL will probably be
   * very directly related to the FullURL value that the VSH already computes.
   * @return The required ultimate URL.
   */
  public String getUltimateURL(String consumerultimatebaseURL, ViewParameters viewparams,
      ViewStateHandler vsh);
}
