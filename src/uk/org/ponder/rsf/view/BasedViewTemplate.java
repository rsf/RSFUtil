/*
 * Created on 13 Sep 2006
 */
package uk.org.ponder.rsf.view;

public interface BasedViewTemplate extends ViewTemplate {
  /** Sets the path of the globally visible resource baseURL to which all
   * relative URLs in this template will appear to be relative. For example,
   * an image URL appearing in the template as ../images/bath.jpg will, when
   * the template is rewritten into the served (generally ViewParameters) URL
   * space, need to be rebased back into the resource space of the application.
   * 
   * Now, in the default case, we are using ServletContext resources, which 
   * accept paths such as /content/templates/viewtemplate.html - therefore
   * in this example the "rebased URL" would be 
   * http://server/context/content/templates . And hence the "fully served
   * URL" at which the above image could be found would be derived by 
   * simply appending the relative URL above to this base URL. In Mond-speak,
   * resourceBase is the "URL which appears to be ."
   */
  public void setResourceBase(String resbase);
  public String getResourceBase();
}
