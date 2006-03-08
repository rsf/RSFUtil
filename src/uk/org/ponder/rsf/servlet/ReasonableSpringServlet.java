/*
 * Created on 01-Feb-2006
 */
package uk.org.ponder.rsf.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsac.servlet.RSACUtils;
import uk.org.ponder.util.Logger;

/**
 * An alternative "all-in-one" main servlet that also performs Spring context
 * loading, for environments where a listener is "too early". Use this servlet
 * if you want to keep web.xml free of clutter (no need for Spring
 * ContextLoaderListener OR RSAC filter).
 * 
 * <p>
 * The main servlet for the RSF system. Hands off immediately to the
 * RootHandlerBean for all logic.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ReasonableSpringServlet extends HttpServlet {
  private ContextLoader contextLoader;
  private RSACBeanLocator rsacbeanlocator;

  private ServletContext sc;

  public void init(ServletConfig config) {
    sc = config.getServletContext();
    Logger.log.warn("ReasonableSpringServlet starting up for context "
        + sc.getRealPath(""));
    contextLoader = new ContextLoader();
    WebApplicationContext wac = contextLoader.initWebApplicationContext(sc);
    rsacbeanlocator = (RSACBeanLocator) wac.getBean("rsacBeanLocator");
  }

  public void destroy() {
    this.contextLoader.closeWebApplicationContext(sc);
  }

  protected void service(HttpServletRequest request,
      HttpServletResponse response) {
    RSACUtils.startServletRequest(request, response, rsacbeanlocator,
        RSACUtils.HTTP_SERVLET_FACTORY);
    try {
      rsacbeanlocator.getBeanLocator().locateBean("rootHandlerBean");
    }
    catch (Throwable t) {
      // Catch and log this here because Tomcat's stack rendering is
      // non-standard and crummy.
      Logger.log.error("Error servicing RSAC request: ", t);
      if (t instanceof Error) {
        throw ((Error)t);
      }
    }
    finally {
      rsacbeanlocator.endRequest();
    }
  }
}
