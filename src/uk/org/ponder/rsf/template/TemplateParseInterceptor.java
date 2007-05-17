/*
 * Created on 19 Aug 2006
 */
package uk.org.ponder.rsf.template;

import java.util.Map;

/** An interface invoked during parsing of a template file, to allow any
 * last-minute adjustment of attributes (typically, to "auto-mark-up" with
 * an rsf:id attribute where there may have been none).
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface TemplateParseInterceptor {
  /** Invoked with a modifiable map of attributes encountered while parsing
   * a tag in a template file. The implementor may perform any adjustment 
   * to the map (although if the adjusted map does NOT contain the 
   * {@link XMLLump#ID_ATTRIBUTE}, the adjustments will be ignored).
   * Do not cache the supplied map after the return of this method.
   */
  public void adjustAttributes(String tag, Map attributes);
}
