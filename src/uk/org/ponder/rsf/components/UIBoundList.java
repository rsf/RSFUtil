/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.uitype.StringArrayUIType;

public class UIBoundList extends UIBound {
  public void setValue(String[] value) {
    this.value = value;
  }
  /** An array of labels used for displaying these values in their rendered
   * container. This must clearly be exactly as long as the list bound as "value".
   */
  public String[] labels;
  /** An EL reference to a bean of type ValueLabelRenderer. If this is null,
  *  the labels will just be duplicated from the list values directly.*/
  public String labelrendererbinding;
  
  public String[] getValue() {
    return (String[]) value;
  }
  
  public UIBoundList() {
    value = StringArrayUIType.instance.getPlaceholder();
  }
}
