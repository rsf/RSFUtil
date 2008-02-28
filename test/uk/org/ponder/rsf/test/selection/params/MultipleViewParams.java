/*
 * Created on 28 Feb 2008
 */
package uk.org.ponder.rsf.test.selection.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class MultipleViewParams extends SimpleViewParameters {
  public int selsize;
  public boolean primitive;
  public Integer[] selected;
  
  public MultipleViewParams() {}
  
  public MultipleViewParams(String viewID, int selsize, Integer[] selected, boolean primitive) {
    this.viewID = viewID;
    this.selsize = selsize;
    this.selected = selected;
    this.primitive = primitive;
  }
}
