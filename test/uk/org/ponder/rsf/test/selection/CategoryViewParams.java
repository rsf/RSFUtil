/*
 * Created on 27 Feb 2008
 */
package uk.org.ponder.rsf.test.selection;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class CategoryViewParams extends SimpleViewParameters {
  public Integer id;
  
  public CategoryViewParams() {}
  
  public CategoryViewParams(String viewID, Integer id) {
    this.viewID = viewID;
    this.id = id;
  }
}
