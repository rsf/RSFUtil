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
 *
 */

public interface EarlyRequestParser {
  /** The parameter map from the request */
  public Map getRequestMap();

  /** The pathinfo as returned from the request. This INCLUDES an initial
   * slash but no final slash.
   */
  public String getPathInfo();

  /**
   * A factory method for a String encoding the nature of the current request
   * cycle, either ViewParameters.RENDER_REQUEST or
   * ViewParameters.ACTION_REQUEST.
   */
  public String getRequestType();

  /** Returns a (possibly empty) Map of parameter names to Spring MultipartFile 
   * for the current request */
  
  public Map getMultipartMap();
}