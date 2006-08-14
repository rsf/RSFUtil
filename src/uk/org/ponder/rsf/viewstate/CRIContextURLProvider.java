/*
 * Created on 14 Aug 2006
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.webapputil.ConsumerInfo;

public class CRIContextURLProvider implements ContextURLProvider {

  private BaseURLProvider bup;
  private ConsumerInfo ciproxy;

  public void setBaseURLProvider(BaseURLProvider bup) {
    this.bup = bup;
  }
  
  public void setConsumerInfo(ConsumerInfo ci) {
    this.ciproxy = ci;
  }
  
  public String getContextBaseURL() {
    ConsumerInfo ci = ciproxy.get();
    String useresurl = bup.getResourceBaseURL();
    if (ci.resourceurlbase != null) {
      useresurl = ci.resourceurlbase;
    }
    return useresurl;
  }

}
