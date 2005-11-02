/*
 * Created on Aug 7, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.UIComponent;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ComponentProcessor {
  /** Returned from processComponent indicating that the component was 
   * untouched by processing.
   */
  public static final int NOT_HANDLED = 0;;
  /** Returned from processComponent indicating that the component was 
   * modified by processing.
   */
  public static final int HANDLED = 1;
  /** Returned from processComponent indicating that the component was 
   * modified by "folding", i.e. shunting as a leaf child and reattaching 
   * its children to the former parent.
   */
  public static final int HANDLED_FOLDED = 2;
  public int processComponent(UIComponent toprocess);
}
