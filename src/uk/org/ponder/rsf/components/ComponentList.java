/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A type-safe list of UIComponent objects.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ComponentList extends ArrayList {
  public static ComponentList EMPTY_LIST = new ComponentList();

  public ComponentList() {}
  
  public ComponentList(UIComponent component) {
    add(component);
  }
  
  public ComponentList(Collection c) {
    addAll(c);
  }
  
  public UIComponent componentAt(int i) {
    return (UIComponent) get(i);
  }

  public boolean add(Object o) {
    UIComponent cast = (UIComponent) o;
    return super.add(cast);
  }

  public ComponentIterator iterate() {
    return new ComponentIterator() {
      int pos = 0;

      public boolean hasMoreComponents() {
        return pos < size();
      }

      public UIComponent nextComponent() {
        return componentAt(pos++);
      }
    };

  }
}
