/*
 * Created on Apr 26, 2006
 */
package uk.org.ponder.rsf.templateresolver;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** A simple TemplateExtensionInferror that always returns the same extension */

public class StaticTemplateExtensionInferrer implements TemplateExtensionInferrer {
  private String extension;
  
  public void setExtension(String extension) {
    this.extension = extension;
  }
  
  public String inferTemplateExtension(ViewParameters viewparams) {
    return extension;
  }

}
