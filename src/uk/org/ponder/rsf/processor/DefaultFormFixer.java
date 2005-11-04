/*
 * Created on Nov 2, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.rsf.componentprocessor.ComponentProcessor;
import uk.org.ponder.rsf.componentprocessor.FormModel;
import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;
import uk.org.ponder.util.UniversalRuntimeException;

/** The basic FormFixer implementation, with 
 * responsibilities for i) setting form submission URL to current view URL
 * ii) Folding form children up into parent, leaving it as a leaf in the tree,
 * iii) registering submitting nested UIBound components into the FormModel.
 * Point iii) must have ALREADY been set up by having the <code>submittingcontrols</code>
 * field of UIForm filled in.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
// WHY do we treat forms in this strange way? We WOULD like IKAT to be
// form-agnostic, and also form nesting is not a "given" for WAP &c.
public class DefaultFormFixer implements ComponentProcessor {
  private ViewParameters viewparams;
  private ViewStateHandler viewstatehandler;
  private FormModel formmodel;
  private View view;

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }
  
  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }
  
  public void setFormModel(FormModel formmodel) {
    this.formmodel = formmodel;
  }
  
  public void setView(View view) {
    this.view = view;
  }
  
  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIForm) {
      UIForm toprocess = (UIForm) toprocesso;
      toprocess.fold();
      toprocess.postURL = viewstatehandler.getFullURL(viewparams);
      for (int i = 0; i < toprocess.submittingcontrols.size(); ++ i) {
        String childid = toprocess.submittingcontrols.stringAt(i);
        UIComponent child = view.getComponent(childid);
        if (!(child instanceof UIBound)) {
          throw UniversalRuntimeException.accumulate(new IllegalArgumentException(), 
              "Component with ID " + childid + " listed as submitting child of form " +
              toprocess.getFullID() + " is not an instance of UIBound");
        }
        formmodel.registerChild(toprocess, (UIBound) child);
      }
    }
  }


}
