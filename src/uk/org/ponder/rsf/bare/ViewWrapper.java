/*
 * Created on 6 Feb 2008
 */
package uk.org.ponder.rsf.bare;

import uk.org.ponder.rsf.componentprocessor.ComponentChildIterator;
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
  
  public UIComponent queryComponent(ComponentQuery query) {
    return queryComponent(viewRoot, query);
  }
  
  
  public UIComponent queryComponent(UIContainer root, ComponentQuery query) {
    ComponentChildIterator cci = new ComponentChildIterator(root, smc);
    
    for (cci.)
  }
}
