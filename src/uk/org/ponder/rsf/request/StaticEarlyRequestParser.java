/*
 * Created on Nov 19, 2006
 */
package uk.org.ponder.rsf.request;

import java.util.Locale;
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
  private Locale locale;

  public StaticEarlyRequestParser(Map multipartMap, String pathInfo,
      Map requestMap, String requestType, String environmentType, Locale locale) {
    this.multipartMap = multipartMap;
    this.pathInfo = pathInfo;
    this.requestMap = requestMap;
    this.requestType = requestType;
    this.locale = locale;
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

  // Not ALL EarlyRequestParsers dispense this directly. Currently it is only
  // SWF, which obliges us to factor this off "early".
  public Locale getLocale() {
    return locale;
  }
  
}
