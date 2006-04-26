/*
 * Created on 13-Mar-2006
 */
package uk.org.ponder.rsf.components;

/** Implements a named navigation point in a rendered page. 
 * Peers, for example, with an HTML anchor as rendered with <a name=>.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIAnchor extends UIBoundString {
  public static UIAnchor make(UIContainer parent, String ID, String initvalue,
      String binding) {
    UIAnchor togo = new UIAnchor();
    if (initvalue != null) {
      togo.setValue(initvalue);
    }
    togo.valuebinding = ELReference.make(binding);
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }

  /**
   * Constructs an unbound text field with the specified initial value. This
   * will not interact with the bean model in any way.
   */
  public static UIAnchor make(UIContainer parent, String ID, String initvalue) {
    return make(parent, ID, initvalue, null);
  }

  /**
   * This constructor creates an output component that will simply select a
   * message already present in the template, free from any further interaction
   * with the code.
   */
  public static UIAnchor make(UIContainer parent, String ID) {
    return make(parent, ID, null, null);
  }

}
