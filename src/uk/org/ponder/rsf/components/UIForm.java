/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.UniversalRuntimeException;


/**
 * Represents a Form (submission domain) in the component tree. Forms are 
 * treated somewhat unusually in that they (may be) generated <it>inline</it> in the
 * component tree by the producer, but are <it>folded away</it> before they
 * are seen by the renderer. The form model applied by the target render
 * technology is embodied by the FormModel, held in the parent View object,
 * also as a request-scope bean.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIForm extends UISimpleContainer {
  /** The URL to which this form will be submitted. This SHOULD be the same
   * as the URL of the page containing the form, minus query parameters. This
   * field need not be filled in by the user.
   */
  public String postURL;
  
  /** A list of the FullIDs of the all controls to be submitted by this form.
   * If this is left blank(empty), it will be filled in by a FormFixer 
   * (currently by looking for all nested controls that are bound). Each of 
   * these controls will be either a UICommand or a UIBound.
   * <p> For the HTML submission model, this field is not strictly required
   * since hidden fields are rendered "alongside" their corresponding controls,
   * but is useful for general consistency and bookkeeping. More relevant, say,
   * for "WAP-like" submission models.
   */ 
  public StringList submittingcontrols;
  
  public static UIForm make(UIContainer parent, String ID) {
    if (!(parent instanceof UIBranchContainer)) {
      throw UniversalRuntimeException.accumulate(new 
          IllegalArgumentException("Immediate parent of UIForm must be a branch container"));
    }
    UIForm togo = new UIForm();
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }
  
  public static UIForm make(UIContainer parent) {
    return make(parent, BasicComponentIDs.BASIC_FORM);
  }
  
}
