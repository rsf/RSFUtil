/*
 * Created on Jul 29, 2005
 */
package uk.org.ponder.rsf.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.util.Logger;

/**
 * The main servlet for the RSF system. Hands off immediately to the 
 * RootHandlerBean for all logic.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ReasonableServlet extends HttpServlet {
  private RSACBeanLocator rsacbeanlocator;
  public void init(ServletConfig config) {
    Logger.log.warn("ReasonableServlet starting up for context "
        + config.getServletContext().getRealPath(""));
    ServletContext sc = config.getServletContext();
    WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sc);
    rsacbeanlocator = (RSACBeanLocator) 
      wac.getBean(RSACBeanLocator.RSAC_BEAN_LOCATOR_NAME);
  }
  
  protected void service(HttpServletRequest requst, HttpServletResponse response) {
    rsacbeanlocator.getBeanLocator().locateBean("rootHandlerBean");
  }
}
