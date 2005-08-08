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
}
