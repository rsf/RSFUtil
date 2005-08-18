/*
 * Created on Aug 8, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.util.AssertionException;

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
  /** Return all child components with the given prefix */
  public List getComponents(String id) {
    return (List) childmap.get(id);
  }
  
  public Iterator children() {
    return childmap.values().iterator();
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
  
  public void startForm(String ID) {
    if (currentform == null) {
      // could even check parents.
      throw new AssertionException("Error starting form - form already in progress");
    }
    currentform = new UIForm();
    currentform.ID = ID;
  }
  
  public void startForm() {
    startForm(BasicComponentIDs.BASIC_FORM);
  }
  
  public void endForm() {
    currentform = null;
  }
  
  public void addComponent(UIComponent toadd) {
    toadd.parent = this;
    if (childmap == null) {
      childmap = new HashMap();
    }
    SplitID split = toadd.ID == null? null : new SplitID(toadd.ID);
    String childkey = toadd.ID == null? NON_PEER_ID : split.prefix;
    if (toadd.ID != null && split.suffix == null) {
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
