/*
 * Created on 6 Feb 2008
 */
package uk.org.ponder.rsf.bare;

import uk.org.ponder.rsf.componentprocessor.ComponentChildIterator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.view.ViewRoot;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;

public class ViewWrapper {
  
  private ViewRoot viewRoot;
  private SAXalizerMappingContext smc;

  public ViewWrapper(ViewRoot viewRoot, SAXalizerMappingContext smc) {
    this.viewRoot = viewRoot;
    this.smc = smc;
  }
  
  
  public UIComponent queryComponent(UIComponent query) {
    return queryComponent(new ComponentQuery(query));
  }
  
  public UIComponent queryComponent(ComponentQuery query) {
    return queryComponent(viewRoot, query);
  }
  
  public UIBranchContainer getViewRoot() {
    return viewRoot;
  }
  
  public UIComponent queryComponent(UIContainer root, ComponentQuery query) {
    ComponentChildIterator cci = new ComponentChildIterator(root, smc);
    
    UIComponent exemplar = query.exemplar;
    
    while (cci.hasMoreComponents()) {
      UIComponent test = cci.nextComponent();
      if (!exemplar.getClass().isAssignableFrom(test.getClass())) continue;
      if (exemplar.ID != null && !test.ID.equals(exemplar.ID)) continue;
      String fullID = exemplar.acquireFullID();
      if (fullID != null && !test.getFullID().equals(fullID)) continue;
      return test;
    }
    return null;
  }
}
