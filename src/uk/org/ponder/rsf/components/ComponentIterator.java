/*
 * Created on 6 Feb 2008
 */
package uk.org.ponder.rsf.components;

/** An iteration interface dispensing UIComponents **/

public interface ComponentIterator {
  public boolean hasMoreComponents();
  
  public UIComponent nextComponent();
  
}
