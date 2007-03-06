/*
 * Created on Jan 16, 2006
 */
package uk.org.ponder.rsf.components;

/** Represents the input of multiple String values from the UI. Note that this
 * component cannot be represented directly in HTML - it may be used either as
 * the "selection" member of a multiple selection control, or passed as the 
 * argument to a fuller Evolver.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIInputMany extends UIBoundList {
  public UIInputMany () {
    fossilize = true;
    willinput = true;
  }
  
  /**
   * A "bare" constructor suitable for the selection member of a multiple
   * selection control (UIInput);
   */
  public static UIInputMany make(String valuebinding) {
    UIInputMany togo = new UIInputMany();
    togo.valuebinding = new ELReference(valuebinding);
    return togo;
  }
  
  /** A full constructor, specifying a bound String array with an optional
   * initial value. Note that there is no direct renderer for such a component
   * in HTML.
   */
  public static UIInputMany make(UIContainer parent, String ID, String binding,
      String[] initvalue) {
    UIInputMany togo = new UIInputMany();
    togo.valuebinding = ELReference.make(binding);
    if (initvalue != null) {
      togo.setValue(initvalue);
    }
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }
  
  /** @see #make(UIContainer, String, String, String[]) */
  
  public static UIInputMany make(UIContainer parent, String ID, String binding) {
    return make(parent, ID, binding, null);
  }
}
