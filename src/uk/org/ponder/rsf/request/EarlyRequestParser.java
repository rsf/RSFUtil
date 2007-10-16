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
   * or a WSRP performBlockingInteration request.
   */
  public static final String ACTION_REQUEST = "action";

  /** Identifies this environment as a javax.servlet Servlets environment **/
  public static final String SERVLET_ENVIRONMENT = "servlet";
  
  /** Identifies this environment as a JSR 168 javax.portlet Portlets environment **/
  public static final String PORTLET_168_ENVIRONMENT = "portlet-168";

  /** Identifies this environment as a JSR 286 javax.portlet Portlets environment **/
  public static final String PORTLET_286_ENVIRONMENT = "portlet-286";
  
  /** Identifies this environment as a Spring Web Flow "forwards" environment **/
  public static final String SPRING_WEBFLOW_ENVIRONMENT = "Spring Web Flow";
  
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
  
  /** Returns one of the <code>_ENVIRONMENT</code> Strings above, if the environment
   * can be determined to be one of the known standard types, or <code>null</code> otherwise. 
   */
  public String getEnvironmentType();
}