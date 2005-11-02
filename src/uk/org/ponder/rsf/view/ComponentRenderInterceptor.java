/*
 * Created on Aug 7, 2005
 */
package uk.org.ponder.rsf.view;

import java.util.HashMap;

import uk.org.ponder.rsf.components.UIComponent;

/**
 * A currently disused interface to allow a last-ditch alteration of 
 * the rendered attributes for a component peer. Deprecated in favour of 
 * {@see uk.org.ponder.rsf.renderer.StaticComponentRenderer}
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ComponentRenderInterceptor {
  public HashMap intercept(String tag, UIComponent component, 
      HashMap attributes);
}
