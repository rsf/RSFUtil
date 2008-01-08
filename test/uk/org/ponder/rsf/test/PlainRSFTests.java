/*
 * Created on 22 Jun 2007
 */
package uk.org.ponder.rsf.test;

import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.rsac.test.AbstractRSACTests;

public class PlainRSFTests extends AbstractRSACTests {

  protected String[] getConfigLocations() {
    return new String[] {
        "classpath:conf/rsf-config.xml", "classpath:conf/blank-applicationContext.xml",
        "classpath:uk/org/ponder/rsf/test/sitemap/sitemap-context.xml"
    };
  }

  protected RequestLauncher requestLauncher;
  
  private RequestLauncher allocateRequestLauncher() {
    return new RequestLauncher(applicationContext, getRSACBeanLocator());
  }
  
  public RequestLauncher getRequestLauncher() {
    if (isSingleShot()) {
      return requestLauncher;
    }
    else {
      return allocateRequestLauncher();
    }
  }
  
  public String[] getRequestConfigLocations() {
    return new String[] {
        "classpath:conf/rsf-requestscope-config.xml",
        "classpath:conf/blank-requestContext.xml"};
  }

  protected void onSetUp() throws Exception {
    WriteableBeanLocator wbl = getRSACBeanLocator().getDeadBeanLocator();
    if (isSingleShot()) {
      requestLauncher = allocateRequestLauncher();
      wbl.set("earlyRequestParser", requestLauncher);
    }
  }
  
}
