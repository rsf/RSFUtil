/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.rsf.processor.GetHandler;
import uk.org.ponder.rsf.processor.PostHandler;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;
import uk.org.ponder.servletutil.HttpServletRequestAware;
import uk.org.ponder.servletutil.HttpServletResponseAware;
import uk.org.ponder.streamutil.write.OutputStreamPOS;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * The RootHandlerBean is the main entry point for handling of the
 * HttpServletRequest cycle. Virtually all servlet-dependent logic is placed in
 * this class, including initiating parameter decoding (eventually done by
 * RSAC), setting up the response writer and issuing client redirects. From
 * here, handling forks to the GetHandler and PostHandler in the
 * <code>processor</code> package.
 * 
 * <p>Nothing should touch the HttpServletRequest before this request-scope
 * factory bean, which parses primitive information such as the request
 * type and parameter map. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RootHandlerBean implements HttpServletRequestAware,
    HttpServletResponseAware {

  private BeanLocator beanlocator;
  private String requesttype;
  private HttpServletRequest request;
  private HttpServletResponse response;

  public void setRequestBeanLocator(BeanLocator beanlocator) {
    this.beanlocator = beanlocator;
  }

  public void setHttpServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public void setHttpServletResponse(HttpServletResponse response) {
    this.response = response;
  }

  public void init() {
    requesttype = request.getMethod().equals("GET")? ViewParameters.RENDER_REQUEST : 
      ViewParameters.ACTION_REQUEST;
    Logger.log.info("begin parseRequest");
    // We INSIST that all data passed in and out should be in UTF-8.
    // TODO: Make sure we do not tread on the toes of a POST request in
    // doing this however.
    try {
      request.setCharacterEncoding("UTF-8");
    }
    catch (UnsupportedEncodingException uee) {
      throw UniversalRuntimeException.accumulate(uee,
          "Fatal internal error: UTF-8 encoding not found");
    }
    
    if (requesttype.equals(ViewParameters.RENDER_REQUEST)) {
      GetHandler gethandler = (GetHandler) beanlocator.locateBean("gethandler");
      handleGet(gethandler);
    }
    else {
      PostHandler posthandler = (PostHandler) beanlocator
          .locateBean("posthandler");
      handlePost(posthandler);
    }
  }
  
  public void handleGet(GetHandler gethandler) {
    PrintOutputStream pos = setupResponseWriter(request, response);
    try {
      ViewParameters redirect = gethandler.handle(pos, beanlocator);

      if (redirect != null) {
        issueRedirect(redirect, response);
      }
    }
    catch (Throwable t) {
      gethandler.renderFatalError(t, pos);
    }
    pos.close();
  }

  public void handlePost(PostHandler posthandler) {

    ViewParameters redirect = posthandler.handle();

    issueRedirect(redirect, response);
  }

  // If this is a web service request, send the required redirect URL
  // to the client via the body of the POST response. Otherwise, issue
  // the redirect directly to the client via this connection.
  // TODO: This method might need some state, depending on the client.
  // maybe we can do this all with "request beans"?
  public void issueRedirect(ViewParameters viewparams,
      HttpServletResponse response) {
    ViewStateHandler viewstatehandler = (ViewStateHandler) beanlocator.locateBean("viewstatehandler");
    String path = viewstatehandler.getFullURL(viewparams);
    Logger.log.info("Redirecting to " + path);
    try {
      response.sendRedirect(path);
    }
    catch (IOException ioe) {
      Logger.log.warn("Error redirecting to URL " + path, ioe);
    }
  }

  public static PrintOutputStream setupResponseWriter(
      HttpServletRequest request, HttpServletResponse response) {
    try {
      response.setContentType("text/html; charset=UTF-8");
      // response.setContentType("application/xhtml+xml; charset=UTF-8");

      OutputStream os = response.getOutputStream();
      PrintOutputStream pos = new OutputStreamPOS(os, "UTF-8");

      String acceptHeader = request.getHeader("Accept");

      // Work-around for JSF 1.0 RI bug: failing to accept "*/*" and
      // and "text/*" as valid replacements for "text/html"
      if (acceptHeader != null
          && (acceptHeader.indexOf("*/*") != -1 || acceptHeader
              .indexOf("text/*") != -1)) {
        acceptHeader += ",text/html";
      }

      String responseencoding = response.getCharacterEncoding();
      response.setHeader("Accept", acceptHeader);
      return pos;
    }
    catch (IOException ioe) {
      throw UniversalRuntimeException.accumulate(ioe,
          "Error setting up response writer");
    }
  }
  
  public Map getRequestMap() {
    return request.getParameterMap();
  }
  
  public String getPathInfo() {
    return request.getPathInfo();
  }

  /** A factory method for a String encoding the nature of the current
   * request cycle, either ViewParameters.RENDER_REQUEST or 
   * ViewParameters.ACTION_REQUEST.
   */
  public String getRequestType() {
    return requesttype;
  }

}