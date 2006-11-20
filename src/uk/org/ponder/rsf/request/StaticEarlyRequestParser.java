/*
 * Created on Nov 19, 2006
 */
package uk.org.ponder.rsf.request;

import java.util.Map;

public class StaticEarlyRequestParser implements EarlyRequestParser {
  private Map multipartMap;
  private String pathInfo;
  private Map requestMap;
  private String requestType;

  public StaticEarlyRequestParser(Map multipartMap, String pathInfo,
      Map requestMap, String requestType) {
    this.multipartMap = multipartMap;
    this.pathInfo = pathInfo;
    this.requestMap = requestMap;
    this.requestType = requestType;
  }

  public Map getMultipartMap() {
    return multipartMap;
  }

  public String getPathInfo() {
    return pathInfo;
  }

  public Map getRequestMap() {
    return requestMap;
  }

  public String getRequestType() {
    return requestType;
  }

}
