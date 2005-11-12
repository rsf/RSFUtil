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

}
