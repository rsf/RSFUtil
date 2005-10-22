/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.rsf.processor.GetHandler;
import uk.org.ponder.rsf.processor.PostHandler;
import uk.org.ponder.springutil.RSACBeanLocator;
import uk.org.ponder.streamutil.OutputStreamPOS;
import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.webapputil.ViewParameters;

/**
 * The RootHandlerBean is the main entry point for handling of the HttpServletRequest
 * cycle. Almost all servlet-dependent logic is placed in this class, including
 * initiating parameter decoding (eventually done by RSAC), setting up the
 * response writer and issuing client redirects. From here, handling forks
 * to the GetHandler and PostHandler in the <code>processor</code> package.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class RootHandlerBean {
  private GetHandler gethandler;
  private PostHandler posthandler;

  private ViewParameters vpexemplar;

  private RSACBeanLocator rsacbeangetter;
  
  public void setViewParametersExemplar(ViewParameters vpexemplar) {
    this.vpexemplar = vpexemplar;
  }

  public void setGetHandler(GetHandler gethandler) {
    this.gethandler = gethandler;
  }

  public void setPostHandler(PostHandler posthandler) {
    this.posthandler = posthandler;
  }
//  
//  public RSACBeanLocator getRequestBeanLocator() {
//    return rsacbeangetter;
//  }
//  
  public void setRequestBeanLocator(RSACBeanLocator rsacbeangetter) {
    this.rsacbeangetter = rsacbeangetter;
  }
  
  public void handleGet(HttpServletRequest request, HttpServletResponse response) {
    PrintOutputStream pos = setupResponseWriter(request, response);
    try {
      ViewParameters redirect = gethandler.handle(pos, 
          rsacbeangetter.getBeanLocator());

      if (redirect != null) {
        issueRedirect(redirect, response);
      }
    }
    catch (Throwable t) {
      gethandler.renderFatalError(t, pos);
    }
    pos.close();
  }

  public void handlePost(HttpServletRequest request,
      HttpServletResponse response) {
    ViewParameters origrequest = ViewParametersFactory.parseRequest(request, vpexemplar);

    ViewParameters redirect = posthandler.handle(origrequest, request.getParameterMap());

    issueRedirect(redirect, response);
  }

  // If this is a web service request, send the required redirect URL
  // to the client via the body of the POST response. Otherwise, issue
  // the redirect directly to the client via this connection.
  // TODO: This method might need some state, depending on the client.
  // maybe we can do this all with "request beans"?
  public static void issueRedirect(ViewParameters viewparams,
      HttpServletResponse response) {
    String path = viewparams.getFullURL();
    Logger.log.info("Redirecting to " + path);
    try {
      response.sendRedirect(path);
    }
    catch (IOException ioe) {
      Logger.log.warn("Error redirecting to URL " + path, ioe);
    }
  }

  public static PrintOutputStream setupResponseWriter(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      response.setContentType("text/html; charset=UTF-8");
      //response.setContentType("application/xhtml+xml; charset=UTF-8");
      
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
}