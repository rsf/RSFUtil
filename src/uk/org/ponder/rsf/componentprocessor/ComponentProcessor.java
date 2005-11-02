/*
 * Created on Aug 7, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.UIComponent;

/**
 * A component processor performs a "fixup" of a component based on some
 * contextual information. Processing is applied by the ViewProcessor, 
 * after tree production by the ComponentProducer and before rendering by
 * the ViewRender. A processor may only perform limited modification of the 
 * component tree - no component may be destroyed, and the only structural
 * modification permitted is "folding" - shunting all children of a non-forking
 * container to its forking parent.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface ComponentProcessor {
  /** Returned from processComponent indicating that an error occurred. 
   * This is a placeholder value that will be assumed in the case handling
   * throws an exception. However, to be graceful, any such exception should be
   * noted in the TargettedMessageList for the current request.
   */
  public static final int ERROR = -1;
  /** Returned from processComponent indicating that the component was 
   * untouched by processing.
   */
  public static final int NOT_HANDLED = 0;
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
