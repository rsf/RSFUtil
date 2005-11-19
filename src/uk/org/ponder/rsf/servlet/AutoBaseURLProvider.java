/*
 * Created on Oct 26, 2005
 */
package uk.org.ponder.rsf.servlet;

import javax.servlet.http.HttpServletRequest;

import uk.org.ponder.rsf.viewstate.BaseURLProvider;
import uk.org.ponder.rsf.viewstate.StaticBaseURLProvider;
import uk.org.ponder.servletutil.HttpServletRequestAware;
import uk.org.ponder.servletutil.ServletUtil;

public class AutoBaseURLProvider implements HttpServletRequestAware,
    BaseURLProvider {
  private HttpServletRequest request;

  public void setHttpServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  private StaticBaseURLProvider sbup;

  public void init() {
    sbup = new StaticBaseURLProvider();
    String baseurl = ServletUtil.getBaseURL2(request);
    String resourcebaseurl = request.getServletPath();
    sbup.setBaseURL(baseurl);
    sbup.setResourceBaseURL(resourcebaseurl);

  }

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
