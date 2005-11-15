/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.components;

public class UISimpleContainer extends UIContainer {
  private ComponentList children = null;

  public ComponentList flattenChildren() {
    if (children == null) {
      children = new ComponentList();
    }
    return children;
  }

  public void addComponent(UIComponent toadd) {
    toadd.parent = this;
    if (children == null) {
      children = new ComponentList();
    }
    children.add(toadd);
  }


  /**
   * "Fold" this container into its parent by shifting all children into the
   * parent. This should only be called during fixup time. Note that this must
   * NOT alter the fullID of any component in the tree!
   * 
   * @see uk.org.ponder.rsf.util.RSFFactory for implementation of computeFullID.
   */
  public void fold() {

    // the parent of an IKATContainer is ALWAYS another IKATContainer.
    //UIContainer parenti = (UIContainer) parent;
    for (int i = 0; i < children.size(); ++ i) {
      UIComponent gchild = children.componentAt(i);
      parent.addComponent(gchild);
    }
    children.clear();
//    parenti.childmap.putAll(childmap);
//    childmap.clear();
  }
  
  public void fold(UIContainer target) {
    
  }
}
