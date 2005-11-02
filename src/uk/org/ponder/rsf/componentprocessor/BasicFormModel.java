/*
 * Created on Nov 1, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;

public class BasicFormModel implements FormModel {
  
  // this is a map of component IDs in this container, to their parent
  // form.
  private Map componentToForm;
  // All this horrid form logic is here, ironically, so that code that
  // oughtn't to know about forms (e.g. parser) should be able to ignore them.
  // Review this when we get a moment.
  private UIContainer getFormHolder() {
    UIContainer search = this;
    while (search != null) {
      if (search.currentform == null) 
        search = search.parent;
      else return search;
    }
    return null;
  }
  // This method is called during PRODUCTION in order to assess whether
  // components should be assigned to a form or not. 
  public UIForm getActiveForm() {
    UIContainer formholder = getFormHolder();
    return formholder == null? null : formholder.currentform;
  }
 
  
  // this is called during RENDERING to find the relevant form.
  // currently disused - will be used by fossilized code somehow.
  public UIForm formForComponent(UIComponent component) {
    UIContainer search = this;
    while (search != null) {
      if (search.componentToForm == null)
        search = search.parent;
      else return (UIForm) search.componentToForm.get(component);
    }
    return null;
  }
  
  /** This field is used as a convenience when building up a component
   * tree in code. It is periodically set to null or otherwise, and the
   * next added component will be ascribed to the mentioned form.
   */
  private transient UIForm currentform;
  
  public void startForm(UIForm form) {
  
    currentform = form;
    if (componentToForm == null) { // don't overwrite any existing map.
      componentToForm = new HashMap();
    }
    // a formID is never repetitive. We put this here so that the renderer can
    // find the form component in the right place when it looks. The fact that
    // the form component is in the "wrong place" in the HTML hierarchy should
    // all come out in the wash.
    childmap.put(form.ID, currentform);
  }

  public void endForm() {
    currentform = null;
  }
  
}
