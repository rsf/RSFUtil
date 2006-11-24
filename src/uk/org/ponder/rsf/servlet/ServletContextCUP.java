/*
 * Created on 16 Oct 2006
 */
package uk.org.ponder.rsf.servlet;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import uk.org.ponder.rsf.viewstate.ContextURLProvider;
import uk.org.ponder.servletutil.ServletUtil;

public class ServletContextCUP implements ContextURLProvider,
    ApplicationContextAware {
  String contextURL;

  public String getContextBaseURL() {
   return contextURL;
  }

  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    WebApplicationContext wac = (WebApplicationContext) applicationContext;
    contextURL = ServletUtil.computeContextName(wac.getServletContext());
  }

}
