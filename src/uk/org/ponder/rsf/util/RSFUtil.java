/*
 * Created on Oct 13, 2004
 */
package uk.org.ponder.rsf.util;

import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIIKATContainer;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.util.AssertionException;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class RSFUtil {
  
  /** This method returns an enclosing Form instance, where one is present
   * in the tree. Note that this only makes sense for HTML-style forms, and
   * is a coding convenience. For alternative form models, client code must
   * keep records of the UIForm instance themselves.
   */
  public static UIForm findBasicForm(UIContainer tocheck) {
    while (tocheck != null) {
      if (tocheck instanceof UIForm) return (UIForm) tocheck;
      tocheck = tocheck.parent;
    }
    return null;
  }
  
  /** A convenience method that assumes the BasicFormModel (uses findBasicForm above).
   * Adds the supplied name/value pair to the nearest enclosing form control.
   */
  
  public static void addBasicFormParameter(UIContainer local, String name, String value) {
    UIForm enclosing = findBasicForm(local);
    if (enclosing == null) {
      throw new AssertionException("Component " + local.getFullID() + " has no form parent!");
    }
    enclosing.hiddenfields.add(new UIParameter(name, value));
  }
  
  /** Determines whether the supplied component has a bound value, and hence
   * will be visible to fossilized bindings/RSVC processing. It must not only
   * be an instance of @see UIBound, but also have a non-null binding.
   */
  public static boolean isBound(UIComponent tocheck) {
    return tocheck instanceof UIBound? ((UIBound)tocheck).valuebinding != null : false;
  }
  
  /** @see UIComponent for the operation of this algorithm.
   */
  
  public static String computeFullID(UIComponent tocompute) {
    StringBuffer togo = new StringBuffer();
    UIComponent move = tocompute;
    while (move.parent != null) {
      if (move instanceof UIIKATContainer) {
        UIIKATContainer movec = (UIIKATContainer)move;
        // only insert naming section for concrete container. 
        if (move.getClass() == UIIKATContainer.class) {
          togo.insert(0, movec.ID.toString() + SplitID.SEPARATOR + movec.localID + SplitID.SEPARATOR);
        }
      }
      else {
        // this will surely be the leaf and requires no colon to separate forwards.
        togo.insert(0, move.ID);
      }
      move = move.parent;
    }
    return togo.toString();
  }


//  public static void addCommandLinkParameter(UICommand trigger, String key,
//      String value) {
//    trigger.parameters.put(key, value);
//  }

}