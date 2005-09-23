/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.view;

import java.io.InputStream;

import uk.org.ponder.rsf.html.ViewRender;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ViewTemplate extends ComponentChecker {
  public void parse(InputStream toparse);
  public ViewRender getViewRender();
  /** Sets the path relative to the webapp root of the file containing this
   * template, in case it makes references using relative URLs to statically
   * served resources. This path has leading slash removed.
   */
  public void setRelativePath(String relpath);
  public String getRelativePath();
}
