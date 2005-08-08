/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.ArrayList;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ComponentList extends ArrayList {
  public UIComponent componentAt(int i) {
    return (UIComponent) get(i);
  }
}
