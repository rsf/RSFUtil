/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.viewstate.ViewParameters;
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

  /** Which type of submission will this form trigger? For ACTION_REQUEST,
   * this will behave like a UICommand (e.g. HTTP POST) - for RENDER_REQUEST
   * like UILink (e.g. HTTP GET).
   */
  public String type = EarlyRequestParser.ACTION_REQUEST;
  
  /** The target view state of this form. For a managed form, this will be
   * filled in by the fixup phrase to be the current view state.
   */
  public ViewParameters viewparams;
  
  /** The URL to which this form will be submitted. This field will be computed
   * by a fixup and need not be filled in by the user.
   */
  public String targetURL;
  
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
  
  /** Creates an "action" form that will receive an RSF submission */
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
  
  /** Creates an "unmanaged" GET form, targetted at the supplied ViewParameters */
  
  public static UIForm make(UIContainer parent, String ID, ViewParameters viewparams) {
    UIForm togo = make(parent, ID);
    togo.viewparams = viewparams;
    togo.type = EarlyRequestParser.RENDER_REQUEST;
    return togo;
  }
  
  public static UIForm make(UIContainer parent) {
    return make(parent, BasicComponentIDs.BASIC_FORM);
  }
  
}
