/*
 * Created on Oct 26, 2005
 */
package uk.org.ponder.rsf.servlet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.rsf.viewstate.BaseURLProvider;
import uk.org.ponder.rsf.viewstate.StaticBaseURLProvider;
import uk.org.ponder.servletutil.HttpServletRequestAware;
import uk.org.ponder.servletutil.ServletUtil;

public class AutoBaseURLProvider implements HttpServletRequestAware, FactoryBean
{
  private HttpServletRequest request;
  public void setHttpServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public Object getObject() throws Exception {
    StaticBaseURLProvider sbup = new StaticBaseURLProvider();
    String baseurl = ServletUtil.getBaseURL2(request);
    String resourcebaseurl = request.getServletPath();
    sbup.setBaseURL(baseurl);
    sbup.setResourceBaseURL(resourcebaseurl);
    return sbup;
  }

  public Class getObjectType() {
    return BaseURLProvider.class;
  }

  public boolean isSingleton() {
    return true;
  }

}
