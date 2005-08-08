/*
 * Created on Jul 29, 2005
 */
package uk.org.ponder.rsf.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import uk.org.ponder.webapputil.ViewParameters;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ReasonableServlet extends HttpServlet {
  private RootHandlerBean rootbean;
  public void init(ServletContext sc) {
    WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sc);
    rootbean = (RootHandlerBean) wac.getBean("roothandlerbean");
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    rootbean.handleGet(request, response);
    
  }
}
