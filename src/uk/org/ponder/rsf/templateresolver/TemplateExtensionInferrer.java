/*
 * Created on Apr 26, 2006
 */
package uk.org.ponder.rsf.templateresolver;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Used to infer the extension for the template file used to render a view */

public interface TemplateExtensionInferrer {
  /** @return The extension for the template file to be used for the supplied 
   * view id, <i>OMITTING</i> the separating period ".".
   */
  public String inferTemplateExtension(ViewParameters viewparams);
}
