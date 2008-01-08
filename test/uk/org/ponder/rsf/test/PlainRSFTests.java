/*
 * Created on 22 Jun 2007
 */
package uk.org.ponder.rsf.test;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.rsac.test.AbstractRSACTests;
import uk.org.ponder.rsf.request.EarlyRequestParser;

public class PlainRSFTests extends AbstractRSACTests implements EarlyRequestParser {

  protected String[] getConfigLocations() {
    return new String[] {
        "classpath:conf/rsf-config.xml", "classpath:conf/blank-applicationContext.xml",
        "classpath:uk/org/ponder/rsf/test/sitemap/sitemap-context.xml"
    };
  }

  public String[] getRequestConfigLocations() {
    return new String[] {
        "classpath:conf/rsf-requestscope-config.xml",
        "classpath:conf/blank-requestContext.xml"};
  }

  public String getEnvironmentType() {
    return EarlyRequestParser.TEST_ENVIRONMENT;
  }

  protected void onSetUp() throws Exception {
    multipartMap = new HashMap();
    pathInfo = "test";
    requestMap = new HashMap();
    requestType = EarlyRequestParser.RENDER_REQUEST;
    WriteableBeanLocator wbl = getRSACBeanLocator().getDeadBeanLocator();
    wbl.set("earlyRequestParser", this);
  }
  
  protected Map multipartMap;
  protected String pathInfo;
  protected Map requestMap;
  protected String requestType;
  
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
