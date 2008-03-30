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

/** Wraps a component tree returned from an RSF test or "bare" cycle. Allows
 * easy querying for components through {@link #queryComponent(UIComponent)} methods,
 * or else direct access to the component tree itselt through {@link #getViewRoot()}.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class ViewWrapper {
  
  private ViewRoot viewRoot;
  private SAXalizerMappingContext smc;

  public ViewWrapper(ViewRoot viewRoot, SAXalizerMappingContext smc) {
    this.viewRoot = viewRoot;
    this.smc = smc;
  }
  
 /** Query for a particular component within the tree, using a direct "query by
  * example" based on the supplied component.
  * @param query A component to be used as a query within the component tree. This
  * will be matched on type, and if set, other fields such as ID or fullID.
  * @return A matched component, if any, or else <code>null</code>
  */  
  public UIComponent queryComponent(UIComponent query) {
    return queryComponent(new ComponentQuery(query));
  }
  
  /** Query for a particular component within the tree, with a particular ID
   * @see #queryComponent(UIComponent)
   */  
   public UIComponent queryComponent(UIComponent query, String ID) {
     query.ID = ID;
     return queryComponent(new ComponentQuery(query));
   }
  
  public UIComponent queryComponent(ComponentQuery query) {
    return queryComponent(viewRoot, query);
  }
  
  /** Returns the root of the entire wrapped component tree **/
  
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
