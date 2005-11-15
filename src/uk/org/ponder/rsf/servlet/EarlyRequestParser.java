/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.servlet;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.servletutil.HttpServletRequestAware;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**

 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class EarlyRequestParser implements HttpServletRequestAware {

  private HttpServletRequest request;
 
  public void setHttpServletRequest(HttpServletRequest request) {
  
  }
 
  
}
