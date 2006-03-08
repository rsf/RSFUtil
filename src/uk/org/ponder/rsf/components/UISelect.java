/*
 * Created on 13-Jan-2006
 */
package uk.org.ponder.rsf.components;

/**
 * Backs a selection control of some kind, where named values are presented in a
 * list to the user. The returned value which is submitted may be a single
 * selection, multiple selection, or empty, depending on the component type in
 * the <code>selection</code> field.
 * <p>
 * The value binding <code>valuebinding</code> in the superclass, if
 * non-empty, will retrieve an object list, which will be supplied during fixup
 * to the resolving beans referenced by <code>nameresolver</code> (which must
 * not be empty in this case) and <code>idresolver</code>. If idresolver is
 * empty, the list is assumed to be a list of IDs already.
 * 
 * @return
 */

public class UISelect extends UIComponent implements FixableComponent {
  public UIBoundList optionlist;
  
  /** A component representing the rendered labels for the list control */
  public UIBoundList optionnames;
  /**
   * The input component representing the actual selected value. Expected to be
   * either UIInput or UIInputMany.
   */
  public UIBound selection;

  /** Creates a non-submitting (output-only) selection control */
  public static UISelect make(UIContainer tofill, String ID, String[] values,
      String[] labels, String value) {
    UISelect togo = new UISelect();
    togo.optionlist = new UIOutputMany();
    togo.ID = ID;
    togo.optionlist.setValue(values);
    togo.optionnames = new UIBoundList();
    togo.optionnames.setValue(labels);
    togo.selection = new UIOutput();
    if (value != null) {
      ((UIOutput) togo.selection).setValue(value);
    }
    tofill.addComponent(togo);
    return togo;
  }
  /** A "skeleton" make method to prepare for more complex constructions */
  public static UISelect make(UIContainer tofill, String ID) {
    UISelect togo = new UISelect();
    tofill.addComponent(togo);
    togo.ID = ID;
    return togo;
  }
  
  public void fixupComponent() {
    if (optionnames != null) {
      if (optionnames.valuebinding == null) {
        optionnames.valuebinding = optionlist.valuebinding;
      }
    }
  }
  /** Constructs a simple selection control, where the submitted values are 
   * identical with the rendered labels
   */
  public static UISelect make(UIContainer tofill, String ID, String[] options, 
      String valuebinding, String initvalue) {
    UISelect togo = new UISelect();
    togo.ID = ID;
    togo.optionlist = togo.optionnames = UIOutputMany.make(options);
    togo.selection = UIInput.make(valuebinding);
    tofill.addComponent(togo);
    return togo;
    
  }
}