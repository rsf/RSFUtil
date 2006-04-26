/*
 * Created on Oct 26, 2005
 */
package uk.org.ponder.rsf.servlet;

import javax.servlet.http.HttpServletRequest;

import uk.org.ponder.rsf.viewstate.BaseURLProvider;
import uk.org.ponder.rsf.viewstate.StaticBaseURLProvider;
import uk.org.ponder.servletutil.ServletUtil;

/** A request-scope bean which automatically infers the base URL to be
 * used for this request from the current HttpServletRequest.
 * <p>It is a little useful for this to be a request-scope bean so that 
 * the computation of the URLs it not performed repeatedly on query. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class AutoBaseURLProvider implements BaseURLProvider {
  private HttpServletRequest request;

  public void setHttpServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  private StaticBaseURLProvider sbup;

  public void init() {
    sbup = new StaticBaseURLProvider();
    String baseurl = ServletUtil.getBaseURL2(request);
    String resourcebaseurl = ServletUtil.getContextBaseURL2(request);
    sbup.setBaseURL(baseurl);
    sbup.setResourceBaseURL(resourcebaseurl);
  }

  /** A one-shot method that will create a static BaseURLProvider good for
   * the current request.
   */
  
  public BaseURLProvider getBaseURLProvider() {
    return sbup;
  }

  public String getBaseURL() {
    return sbup.getBaseURL();
  }

  public String getResourceBaseURL() {
    return sbup.getResourceBaseURL();
  }

}
