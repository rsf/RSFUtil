/*
 * Created on 10 Aug 2006
 */
package uk.org.ponder.rsf.viewstate;

public class StaticInternalBaseURLProvider implements InternalBaseURLProvider {
  private String internalBaseURL = "";
  
  public void setInternalBaseURL(String internalBaseURL) {
    this.internalBaseURL = internalBaseURL;
  }
  public String getInternalBaseURL() {
    return internalBaseURL;
  }

}
