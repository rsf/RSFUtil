/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.uitype.StringArrayUIType;

/** A bound array of Strings, suitable for being the backing for a selection
 * list, or the selection control for a multiple selection list. Is capable
 * of extracting the value field from a bean collection retrieved via EL. **/
public class UIBoundList extends UIBound {
  public void setValue(String[] value) {
    if (value == null) {
      throw new IllegalArgumentException("Value of UIBoundList cannot be null");
    }
    this.value = value;
  }
  
  public String[] getValue() {
    return (String[]) value;
  }
  
  public UIBoundList() {
    value = StringArrayUIType.instance.getPlaceholder();
  }
  
}
