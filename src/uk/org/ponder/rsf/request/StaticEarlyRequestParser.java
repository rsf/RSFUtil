/*
 * Created on Nov 19, 2006
 */
package uk.org.ponder.rsf.request;

import java.util.Map;
/** A static implementation of EarlyRequestParser which returns already
 * determined values. Most useful with the {@link LazarusRedirector}.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class StaticEarlyRequestParser implements EarlyRequestParser {
  private Map multipartMap;
  private String pathInfo;
  private Map requestMap;
  private String requestType;
  private String environmentType;

  public StaticEarlyRequestParser(Map multipartMap, String pathInfo,
      Map requestMap, String requestType, String environmentType) {
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

  public String getEnvironmentType() {
    return environmentType;
  }

  
}
