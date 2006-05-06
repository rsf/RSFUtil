/*
 * Created on May 6, 2006
 */
package uk.org.ponder.rsf.content;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** A simple ContentTypeResolver that always resolves to the same type */

public class StaticContentTypeResolver implements ContentTypeResolver {
  private String statictype;

  public void setContentType(String statictype) {
    this.statictype = statictype;
  }
  
  public String resolveContentType(ViewParameters viewparams) {
    return statictype;
  }
}
