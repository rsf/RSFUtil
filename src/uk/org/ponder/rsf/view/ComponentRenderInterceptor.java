/*
 * Created on Aug 7, 2005
 */
package uk.org.ponder.rsf.view;

import java.util.HashMap;

import uk.org.ponder.rsf.components.UIComponent;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ComponentRenderInterceptor {
  public HashMap intercept(String tag, UIComponent component, 
      HashMap attributes);
}
