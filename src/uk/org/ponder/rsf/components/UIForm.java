/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.stringutil.StringList;


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
  /** A list of key/value pairs to appear in the request map once this form
   * is submitted. This will accrete 
   */
  public ParameterList hiddenfields = new ParameterList();
  /** A list of the FullIDs of the all controls to be submitted by this form.
   * If this is left blank(empty), it will be filled in by a FormFixer 
   * (currently by looking for all nested controls that are bound).
   */ 
  // TODO: The SAXalizer cannot cope with container members that are null,
  // but we would really like this being EMPTY = NULL to be the condition for
  // infilling.
  public StringList submittingcontrols = new StringList();
  
  
  public static UIForm make(UIBranchContainer parent, String ID) {
    UIForm togo = new UIForm();
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }
  
  public static UIForm make(UIBranchContainer parent) {
    return make(parent, BasicComponentIDs.BASIC_FORM);
  }
  
}
