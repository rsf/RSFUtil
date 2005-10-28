/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.viewstate;


public class StaticBaseURLProvider implements BaseURLProvider {

  private String baseurl;
  private String resourcebaseurl;
  
  /** Sets the default base URL for this view state handler, including trailing
   * slash. However, if a URL has been registered for this request via 
   * ServletUtil.setRequestConsumerURLBase, that one will be used by preference.
   */
  public void setBaseURL(String baseurl) {
    this.baseurl = baseurl;
  }
  
  public String getBaseURL() {
    return baseurl;
  }
  
  /** Sets the default base URL for static resources, including trailing
   * slash. As with baseURL, this may be overridden by ServletUtil.
   */
  public void setResourceBaseURL(String resourcebaseurl) {
    this.resourcebaseurl = resourcebaseurl;
  }
  
  public String getResourceBaseURL() {
    return resourcebaseurl;
  }
  
  

}
