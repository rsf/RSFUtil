/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * Input of a single String-typed value. May
 * peer, in HTML, for example, with &lt;input type="text" or with
 * &lt;textarea&gt;.
 * 
 * @since RSF-0.5
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIInput extends UIBoundString {
  public UIInput() {
    fossilize = true;
    willinput = true;
  }

  /**
   * Construct a new UIInput component with the specified container as parent.
   * 
   * @param parent Parent container to which the component is to be added.
   * @param ID (RSF) ID of this component.
   * @param binding An EL expression to be used as the value binding for the
   *          contained String value. May be <code>null</code>.
   * @param initvalue An initial value for the bound value. May be left
   *          <code>null</code>. If neither this field nor
   *          <code>binding</code> is set. the value present in the template
   *          will be used.
   * @return The constructed UIInput component.
   */
  public static UIInput make(UIContainer parent, String ID, String binding,
      String initvalue) {
    UIInput togo = new UIInput();
    togo.valuebinding = ELReference.make(binding);
    if (initvalue != null) {
      togo.setValue(initvalue);
    }
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }

  public static UIInput make(UIContainer parent, String ID, String binding) {
    return make(parent, ID, binding, null);
  }

  /**
   * A "bare" constructor suitable for the selection member of a single
   * selection control (UIInput);
   * @param valuebinding An EL reference to which the bound value is to be associated
   * @return The constructed UIInput control.
   */
  public static UIInput make(String valuebinding) {
    UIInput togo = new UIInput();
    togo.valuebinding = ELReference.make(valuebinding);
    return togo;
  }
  
  /**
   * @see #make(String)
   * @param valuebinding An EL reference to which the bound value is to be associated
   * @param initvalue An initial value for the control, to take priority over any bound
   * value during the render cycle.
   * @return The constructed UIInput control.
   */
  public static UIInput make(String valuebinding, String initvalue) {
    UIInput togo = new UIInput();
    togo.valuebinding = ELReference.make(valuebinding);
    if (initvalue != null) {
      togo.setValue(initvalue);
    }
    return togo;    
  }
}
