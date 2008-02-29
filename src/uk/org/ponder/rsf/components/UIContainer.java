/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.rsf.util.SplitID;
import uk.org.ponder.stringutil.CharWrap;

/** The base class of all RSF components which may form containment in the
 * component tree. Note that containment may occur in a rendered tag structure
 * which is not reflected by this containment relation. 
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public abstract class UIContainer extends UIParameterHolder {
  /**
   * The localID allows clients to distinguish between multiple instantiations
   * of the "same" (by rsf:id) component within the same scope. It forms part of
   * the global path constructed by getFullID() which uniquely identifies the
   * component.
   */
  public String localID = "";
  
  /** When set to <code>true</code>, this container will be allocated
   * no ID segment at all in the fullID derived for components contained
   * inside it.
   */
  public boolean noID = false;
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

  /**
   * Return the single component with the given ID. This should be an ID without
   * colon designating a leaf child.
   */
  public UIComponent getComponent(String id) {
    if (childmap == null)
      return null;
    Object togo = childmap.get(id);
    if (togo != null && !(togo instanceof UIComponent)) {
      throw new IllegalArgumentException(
          "Error in view tree: component with id " + id
              + " was expected to be a leaf component but was a branch."
              + "\n (did you forget to use a colon in the view template?)");
    }
    return (UIComponent) togo;
  }

  /**
   * Return all child components with the given prefix. This may be either a 
   * List if the component genuinely represents a branch structure, or a single
   * component if a leaf.
   */
  public Object getComponents(String id) {
    return childmap.get(id);
  }

  public String debugChildren() {
    CharWrap togo = new CharWrap();
    togo.append("Child IDs: (");
    UIComponent[] children = flatChildren();
    for (int i = 0; i < children.length; ++i) {
      if (i != 0) {
        togo.append(", ");
      }
      togo.append(children[i].ID);
    }
    togo.append(")");
    return togo.toString();
  }

  /**
   * Returns a flattened array of all children of this container. Note that this
   * method will trigger the creation of a cached internal array on its first
   * use, which cannot be recreated. It is essential therefore that it only be
   * used once ALL modifications to the component tree have concluded (i.e. once
   * rendering starts).
   */
  public UIComponent[] flatChildren() {
    if (flatchildren == null) {
      ComponentList children = flattenChildren();
      flatchildren = (UIComponent[]) children.toArray(new UIComponent[children
          .size()]);
    }
    return flatchildren;
  }

  /**
   * Returns a list of all CURRENT children of this container. This method is
   * safe to use at any time.
   */
  // There are now two calls to this in the codebase, firstly from ViewProcessor
  // and then from BasicFormFixer. The VP call is necessary since it needs to
  // fossilize
  // the list up front, but if another call arises as in BFF we ought to write a
  // multi-iterator.
  public ComponentList flattenChildren() {
    ComponentList children = new ComponentList();
    for (Iterator childit = childmap.values().iterator(); childit.hasNext();) {
      Object child = childit.next();
      if (child instanceof UIComponent) {
        children.add(child);
      }
      else if (child instanceof List) {
        children.addAll((List) child);
      }
    }
    return children;
  }

  /** Add a component as a new child of this container */

  public void addComponent(UIComponent toadd) {
    toadd.parent = this;

    SplitID split = new SplitID(toadd.ID);
    String childkey = split.prefix;
    if (toadd.ID != null && split.suffix == null) {
      childmap.put(childkey, toadd);
    }
    else {
      List children = (List) childmap.get(childkey);
      if (children == null) {
        children = new ArrayList();
        childmap.put(childkey, children);
      }
      else if (!children.isEmpty() && toadd instanceof UIBranchContainer) {
        UIBranchContainer addbranch = (UIBranchContainer) toadd;
        if (addbranch.localID == "") {
          addbranch.localID = Integer.toString(children.size());
        }
      }
      children.add(toadd);
    }
  }

  /** Detach the specific component from its position as a child of this
   * container.
   * @param tomove The component to remove.
   */
  
  public void remove(UIComponent tomove) {
    SplitID split = new SplitID(tomove.ID);
    String childkey = split.prefix;
    if (split.suffix == null) {
      Object tomovetest = childmap.remove(childkey);
      if (tomove != tomovetest) {
        RSFUtil.failRemove(tomove);
      }
    }
    else {
      List children = (List) childmap.get(childkey);
      if (children == null) {
        RSFUtil.failRemove(tomove);
      }
      boolean removed = children.remove(tomove);
      if (!removed)
        RSFUtil.failRemove(tomove);
    }
    tomove.updateFullID(null); // remove cached ID
  }

}
