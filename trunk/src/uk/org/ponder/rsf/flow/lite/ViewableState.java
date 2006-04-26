/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

public abstract class ViewableState extends State {
  public String viewID;

  public void init() {
    if (viewID == null) {
      viewID = id; 
    }
  }
}
