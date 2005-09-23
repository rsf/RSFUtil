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

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ReasonableServlet extends HttpServlet {
  private RootHandlerBean rootbean;
  public void init(ServletConfig config) {
    ServletContext sc = config.getServletContext();
    WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sc);
    rootbean = (RootHandlerBean) wac.getBean("roothandlerbean");
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    rootbean.handleGet(request, response); 
  }
  

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    rootbean.handlePost(request, response); 
  }
  
}
