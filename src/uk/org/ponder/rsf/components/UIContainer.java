/*
 * Created on Aug 8, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIContainer extends UIComponent {
  // This is a map to either the single component with a given ID prefix, or a 
  // list in the case of a repetitive domain (non-null suffix)
  private Map childmap;
 
  public UIComponent getComponent(String id) {
    return (UIComponent) childmap.get(id);
  }
  
  public List getComponents(String id) {
    return (List) childmap.get(id);
  }
  
  // this is a map of component IDs in this container, to their parent
  // form.
  private Map componentToForm;
  
  public UIForm formForComponent(UIComponent component) {
    return (UIForm) componentToForm.get(component);
  }
  
  /** This field is used as a convenience when building up a component
   * tree in code. It is periodically set to null or otherwise, and the
   * next added component will be ascribed to the mentioned form.
   */
  public transient UIForm currentform;
  
  public void addComponent(UIComponent toadd) {
    toadd.parent = this;
    if (childmap == null) {
      childmap = new HashMap();
    }
    String childkey = toadd.ID == null? NON_PEER_ID : toadd.ID.prefix;
    if (toadd.ID != null && toadd.ID.suffix == null) {
      childmap.put(childkey, toadd);
    }
    else {
      List children = (List) childmap.get(childkey);
      if (children == null) {
        children = new ArrayList();
        childmap.put(childkey, children);
      }
      children.add(toadd);
    }
    if (currentform != null) {
      if (componentToForm != null) {
        componentToForm = new HashMap();
      }
      componentToForm.put(toadd, currentform);
    }
  }
}
