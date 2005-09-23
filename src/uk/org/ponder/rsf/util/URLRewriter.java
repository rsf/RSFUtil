/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.util;

import uk.org.ponder.util.Logger;
import uk.org.ponder.webapputil.ViewParameters;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class URLRewriter {
  private ViewParameters exemplar;
  public void setViewParametersExemplar(ViewParameters exemplar) {
    this.exemplar = exemplar;
  }
  /** Returns null for URLs which do not need rewriting. relpath has leading
   * slash removed.
   */
  public String rewriteResourceURL(String path, String relpath) {
    String resourceURL = null;
    // erm?? Was this always broken? Looks like all relative URLs mapped onto
    // the default base URL???
    if (path.charAt(0) != '/') {
      resourceURL = exemplar.getViewStateHandler().getResourceURL(relpath + path);
    }
    else {
      //String resourceURL = origViewHandler.getResourceURL(context, path);
      resourceURL = exemplar.getViewStateHandler().getResourceURL(
          path.substring(1));
    }
    if (Logger.log.isInfoEnabled()) {
      Logger.log.info("getResourceURL returning " + resourceURL + " for path "
        + path);
    }
    return resourceURL; 
  }
}
