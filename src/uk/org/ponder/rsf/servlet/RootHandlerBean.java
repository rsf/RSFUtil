/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.rsf.processor.GetHandler;
import uk.org.ponder.rsf.processor.PostHandler;
import uk.org.ponder.rsf.state.RequestStateEntry;
import uk.org.ponder.springutil.RSACBeanGetter;
import uk.org.ponder.streamutil.OutputStreamPOS;
import uk.org.ponder.streamutil.PrintOutputStream;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.webapputil.ViewParameters;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class RootHandlerBean {
  private GetHandler gethandler;
  private PostHandler posthandler;

  private ViewParameters vpexemplar;

  private RSACBeanGetter rsacbeangetter;
  
  public void setViewParametersExemplar(ViewParameters vpexemplar) {
    this.vpexemplar = vpexemplar;
  }

  public void setGetHandler(GetHandler gethandler) {
    this.gethandler = gethandler;
  }

  public void setPostHandler(PostHandler posthandler) {
    this.posthandler = posthandler;
  }
  
  public RSACBeanGetter getRequestBeanGetter() {
    return rsacbeangetter;
  }
  
  public void setRSACBeanGetter(RSACBeanGetter rsacbeangetter) {
    this.rsacbeangetter = rsacbeangetter;
  }
  
  public void handleGet(HttpServletRequest request, HttpServletResponse response) {
    PrintOutputStream pos = setupResponseWriter(request, response);
    try {
      ViewParameters redirect = gethandler.handle(pos, 
          rsacbeangetter.getBeanGetter());

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
  public void issueRedirect(ViewParameters viewparams,
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

  private PrintOutputStream setupResponseWriter(HttpServletRequest request,
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