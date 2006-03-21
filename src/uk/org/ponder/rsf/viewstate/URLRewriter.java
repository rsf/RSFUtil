/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.util.Logger;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class URLRewriter {
  private ViewStateHandler viewstatehandler;
  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }
  /** Returns null for URLs which do not need rewriting. relpath has leading
   * slash removed.
   */
  public String rewriteResourceURL(String path, String relpath) {
    String resourceURL = null;
    // erm?? Was this always broken? Looks like all relative URLs mapped onto
    // the default base URL???
    if (path.charAt(0) != '/') {
      resourceURL = viewstatehandler.getResourceURL(relpath + path);
    }
    else {
      //String resourceURL = origViewHandler.getResourceURL(context, path);
      resourceURL = viewstatehandler.getResourceURL(
          path.substring(1));
    }
    if (Logger.log.isDebugEnabled()) {
      Logger.log.debug("getResourceURL returning " + resourceURL + " for path "
        + path);
    }
    return resourceURL; 
  }
}
