/*
 * Created on Aug 8, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.stringutil.CharWrap;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * UIContainer represents a "branch point" in the IKAT rendering process,
 * rather than simply just a level of component containment.
 * <p>UIContainer has responsibility for managing naming of child components, 
 * as well as separate and parallel responsibility for forms.
 * The key to the child map is the ID prefix - if the ID has no suffix, the
 * value is the single component with that ID at this level. If the ID
 * has a suffix, indicating a repetitive domain, the value is an ordered
 * list of components provided by the producer which will drive the 
 * rendering at this recursion level.
 * <p>It is assumed that an ID prefix is globally unique within the tree, not
 * just within its own recursion level - i.e. IKAT resolution takes place
 * over ALL components sharing a prefix throughout the template. This is "safe"
 * since "execution" will always return to the call site once the base
 * (XML) nesting level at the target is reached again.
 */
public class UIContainer extends UIComponent {
  /** The localID allows clients to distinguish between multiple instantiations
   * of the "same" (by rsf:id) component within the same scope. It forms part
   * of the global path constructed by getFullID() which uniquely identifies
   * the component.
   */
  public String localID = "";
  // This is a map to either the single component with a given ID prefix, or a 
  // list in the case of a repetitive domain (non-null suffix)
  private Map childmap = new HashMap();
  
  // this is created by the first call to flatChildren() which is assumed to
  // occur during the render phase. Implicit model that component tree is
  // i) constructed, ii) rendered, iii) discarded.
  // It is worth caching this since it is iterated over up to 4n times during
  // rendering, for each HTMLLump headlump that matches the requested call
  // in the 4 scopes.
  private transient UIComponent[] flatchildren;
 
  public UIComponent getComponent(String id) {
    return (UIComponent) childmap.get(id);
  }
  /** Return all child components with the given prefix */
  public List getComponents(String id) {
    return (List) childmap.get(id);
  }
  
  public String debugChildren() {
    CharWrap togo = new CharWrap();
    togo.append("Child IDs: (");
    UIComponent[] children = flatChildren();
    for (int i = 0; i < children.length; ++ i) {
      if (i != 0) {
        togo.append(", ");
      }
      togo.append(children[i].ID);
    }
    togo.append(")");
    return togo.toString();
  }
  
  public UIComponent[] flatChildren() {
    if (flatchildren == null) {
      flatchildren = flattenChildren();
    }
    return flatchildren;
  }
  
  private UIComponent[] flattenChildren() {
    ArrayList children = new ArrayList();
    for (Iterator childit = childmap.values().iterator(); childit.hasNext(); ) {
      Object child = childit.next();
      if (child instanceof UIComponent) {
        children.add(child);
      }
      else if (child instanceof List) {
        children.addAll((List)child);
      }
    }
   return (UIComponent[]) children.toArray(new UIComponent[children.size()]);  
  }
   
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
  
  // this is a map of component IDs in this container, to their parent
  // form.
  private Map componentToForm;
  
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
  
  public void addComponent(UIComponent toadd) {
    toadd.parent = this;
//    if (childmap == null) {
//      childmap = new HashMap();
//    }
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
    UIContainer formholder = getFormHolder();
    if (formholder != null) {  
      formholder.componentToForm.put(toadd, currentform);
    }
  }
}
