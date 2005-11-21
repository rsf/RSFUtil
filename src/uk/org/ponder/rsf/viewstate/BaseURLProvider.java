/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.viewstate;

public interface BaseURLProvider {
  /** Get the base URL that will be used to operate the ViewParameters
   * system - in general this will be the URL to which the ReasonableServlet
   * is mapped in web.xml. This URL DOES include a trailing slash. As
   * an invariant, if this servlet is not being remapped or redespatched in
   * any way, it will be the value returned by 
   * ServletUtil.getBaseURL2(), as implemented by AutoBaseURLProvider. 
   */
  public String getBaseURL();
  /** Get the base URL that will be used to map the base of the static resources
   * served by this webapp. Without support from the URLRewriteSCR, this will
   * have to be the base path of the entire Servlet context.
   */
  public String getResourceBaseURL();
}
