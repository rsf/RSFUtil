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

public class UISelect extends UIBoundList {
  public static final String NAMES_ID_SUFFIX = "-names";
  public static final String SELECTION_ID_SUFFIX = "-selection";
  /** A component representing the rendered labels for the list control */
  public UIBoundList names;

  /**
   * The input component representing the actual selected value. Expected to be
   * either UIInput or UIInputMany.
   */
  public UIBound selection;

  /** Creates a non-submitting (output-only) selection control */
  public static UISelect make(UIContainer tofill, String ID, String[] values,
      String[] labels, String value) {
    UISelect togo = new UISelect();
    togo.ID = ID;
    togo.setValue(values);
    togo.names = new UIBoundList();
    togo.names.ID = ID + NAMES_ID_SUFFIX;
    togo.names.setValue(labels);
    togo.selection = new UIOutput();
    togo.selection.ID = ID + SELECTION_ID_SUFFIX;
    if (value != null) {
      ((UIOutput) togo.selection).setValue(value);
    }
    tofill.addComponent(togo);
    return togo;
  }
}
