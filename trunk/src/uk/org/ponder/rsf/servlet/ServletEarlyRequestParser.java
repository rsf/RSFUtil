/*
 * Created on Nov 19, 2005
 */
package uk.org.ponder.rsf.servlet;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/** 
* <p>Nothing should touch the HttpServletRequest before this request-scope
* factory bean, which parses primitive information such as the request
* type and parameter map. 
**/

public class ServletEarlyRequestParser implements EarlyRequestParser {
  protected HttpServletRequest request;

  public void setHttpServletRequest(HttpServletRequest request) {
    this.request = request;

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
  }
  
  public Map getRequestMap() {
    return request.getParameterMap();
  }
/** The pathinfo as returned from the request. This INCLUDES an initial
 * slash but no final slash.
 */
  public String getPathInfo() {
    String pathinfo = request.getPathInfo();
    return pathinfo == null ? "/" : pathinfo;
  }

  /**
   * A factory method for a String encoding the nature of the current request
   * cycle, either ViewParameters.RENDER_REQUEST or
   * ViewParameters.ACTION_REQUEST.
   */
  public String getRequestType() {
    String requesttype = request.getMethod().equals("GET") ? ViewParameters.RENDER_REQUEST
        : ViewParameters.ACTION_REQUEST;
    return requesttype;
  }

}
