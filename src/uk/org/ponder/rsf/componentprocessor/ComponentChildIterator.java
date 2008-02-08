/*
 * Created on 6 Feb 2008
 */
package uk.org.ponder.rsf.componentprocessor;

import java.util.ArrayList;
import java.util.List;

import uk.org.ponder.rsf.components.ComponentIterator;
import uk.org.ponder.rsf.components.ComponentList;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;

public class ComponentChildIterator implements ComponentIterator {
  // a list of ComponentIterator
  private List itlist = new ArrayList();
  private UIComponent pending;
  private SAXalizerMappingContext smc;
  
  public ComponentChildIterator(UIComponent parent, SAXalizerMappingContext smc) {
    pending = parent;
    this.smc = smc;
  }

  private ComponentIterator iteriseComponent(UIComponent parent) {
    if (parent instanceof UIContainer) {
      return ((UIContainer) parent).flattenChildren().iterate();
    }
    else {
      ConcreteChildIterator cci = new ConcreteChildIterator(parent, smc);
      return new ComponentList(cci.children()).iterate();
    }
  }

  private void consume() {
    pending = null;
    while (!itlist.isEmpty()) {
      ComponentIterator it = (ComponentIterator) itlist.get(itlist.size() - 1);
      if (it.hasMoreComponents()) {
        pending = it.nextComponent();
        break;
      }
      itlist.remove(itlist.size() - 1);
    }  
  }
  
  public boolean hasMoreComponents() {
    return pending != null;
  }

  public UIComponent nextComponent() {
    UIComponent togo = pending;
    itlist.add(iteriseComponent(togo));
    
    consume();
    
    return togo;
  }
}
