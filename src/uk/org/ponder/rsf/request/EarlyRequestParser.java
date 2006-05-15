/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.rsf.request;

import java.util.Map;

/** Interface defining (almost) all of the information required by RSF from
 * our request provider (whether Servlet/Portlet &c). The remaining information
 * comes though BaseURLProvider, and if in use, a session dependence through
 * InSessionTokenRequestState.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface EarlyRequestParser {
  /** Identifies this request as part of a "render" cycle - for a simple HTTP
   * servlet, corresponds to a GET, a JSR168 "render" request or a WSRP 
   * getMarkup request.
   */ 
  public static final String RENDER_REQUEST = "render";
  /** Identifies this request as part of an "action" cycle - for a simple
   * HTTP servlet, corresponds to a POST, a JSR168 "processAction" request 
   * or a WRSP performBlockingInteration request.
   */
  public static final String ACTION_REQUEST = "action";

  /** The parameter map from the request */
  public Map getRequestMap();

  /** The pathinfo as returned from the request. This INCLUDES an initial
   * slash but no final slash.
   */
  public String getPathInfo();

  /**
   * A factory method for a String encoding the nature of the current request
   * cycle, either RENDER_REQUEST or ACTION_REQUEST.
   */
  public String getRequestType();

  /** Returns a (possibly empty) Map of parameter names to Spring MultipartFile 
   * for the current request */
  
  public Map getMultipartMap();
}