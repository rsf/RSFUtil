/*
 * Created on Aug 7, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.UIComponent;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ComponentSetter {
  public void setValue(UIComponent toset, Object value);
}
