/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.viewstate;

public interface BaseURLProvider {
  /** Get the base URL that will be used to operate the ViewParameters
   * system - in general this will be the URL to which the ReasonableServlet
   * is mapped in web.xml.
   */
  public String getBaseURL();
  /** Get the base URL that will be used to map the base of the static resources
   * served by this webapp. Without support from the URLRewriteSCR, this will
   * have to be the base path of the entire Servlet context.
   */
  public String getResourceBaseURL();
}
