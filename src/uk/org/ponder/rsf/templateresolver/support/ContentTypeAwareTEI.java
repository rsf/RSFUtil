/*
 * Created on 11 Oct 2006
 */
package uk.org.ponder.rsf.templateresolver.support;

import uk.org.ponder.rsf.content.ContentTypeInfo;
import uk.org.ponder.rsf.templateresolver.TemplateExtensionInferrer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ContentTypeAwareTEI implements TemplateExtensionInferrer {
  
  private ContentTypeInfo contentTypeInfo;
  
  public void setContentTypeInfo(ContentTypeInfo contentTypeInfo) {
    this.contentTypeInfo = contentTypeInfo;
  }

  public String inferTemplateExtension(ViewParameters viewparams) {
    return contentTypeInfo.get().fileextension;
  }

}
