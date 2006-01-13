/*
 * Created on 13-Jan-2006
 */
package uk.org.ponder.rsf.components;

/** 
 * Backs a selection control of some kind, where named values are presented
 * in a list to the user. The returned value which is submitted may be a 
 * single selection, multiple selection, or empty, depending on the component
 * type in the <code>selection</code> field. 
 * <p>The value binding <code>valuebinding</code> in the superclass, if non-empty,
 * will retrieve an object list, which will be supplied during fixup to the
 * resolving beans referenced by <code>nameresolver</code> (which must not be empty
 * in this case) and <code>idresolver</code>. If idresolver is empty, the list
 * is assumed to be a list of IDs already.  
 * @return
 */

public class UISelect extends UIBound {
  
  
  public UISelectItem[] getValue() {
    return (UISelectItem[]) value;
  }

  public void setValue(UISelectItem[] value) {
    this.value = value;
  }
  /** An EL reference to a bean capable of resolving a list member to its name
   * required for the <code>text<code> field of the SelectItem.
   */
  public String nameresolver;
  /** An EL reference to a bean capable of resolving a list member to its
   * submitted value, probably its ID.
   */
  public String idresolver;
  
  /** The input component representing the actual selected value. May be
   * <code>null</code> if this is an output-only selection control */
  public UIBound selection;
}
