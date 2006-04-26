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
  /** Process the supplied component. Any exception thrown by this method will
   * be logged but will not propagate. In order to gracefully signal an error,
   * it should be appended to the TargettedErrorList for this request.
   * The only permitted structural modification of the tree is a "fold" of a 
   * component STRICTLY DERIVED from UIContainer (e.g. UIForm) into its
   * UIContainer parent. 
   */

  public void processComponent(UIComponent toprocess);
}
