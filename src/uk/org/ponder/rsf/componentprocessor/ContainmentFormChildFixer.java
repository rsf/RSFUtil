/*
 * Created on Nov 2, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.ComponentList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.rsf.util.RSFUtil;

/** A fixer to be run BEFORE the main form fixer, which implements the HTML/HTTP
 * form model whereby ALL nested child controls of the form are submitted. 
 * This fixer will perform no action if it discovers that the "submittingcontrols"
 * fields of {@see uk.org.ponder.rsf.components.UIForm} has already been filled in.
 * <p>
 * This fixer is HTML/HTTP SPECIFIC, and should not execute for other idioms.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class ContainmentFormChildFixer implements ComponentProcessor {

  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIForm) {
      UIForm toprocess = (UIForm) toprocesso;
      if (toprocess.submittingcontrols.size() == 0) {
        // we would really like this to be a check for NULL, but the SAXalizer
        // could not cope with this.
        registerContainer(toprocess, toprocess);
      }
    }
  }
  
  private void registerContainer(UIForm toprocess, UIContainer toregister) {
    ComponentList children = toregister.flattenChildren();
    for (int i = 0; i < children.size(); ++ i) {
      UIComponent child = children.componentAt(i);
      if (RSFUtil.isBound(child)) {
        toprocess.submittingcontrols.add(child.getFullID());
      }
      // TODO: clarify UIForm/UICommand relationship for WAP-style forms. 
      // Who is to be master!
      // we expect that UIForm -> do, UICommand -> go 
      // http://www.codehelp.co.uk/html/wap1.html
      else if (child instanceof UICommand) {
        toprocess.submittingcontrols.add(child.getFullID());
       // add the notation explaining which control is submitting, when it does
        UICommand command = (UICommand) child;
        command.parameters.add(new UIParameter(SubmittedValueEntry.SUBMITTING_CONTROL, 
            child.getFullID()));
        command.parameters.add(new UIParameter(SubmittedValueEntry.FAST_TRACK_ACTION,
            command.methodbinding.value));
      }
      else if (child instanceof UIBranchContainer) {
        registerContainer(toprocess, (UIBranchContainer) child);
      }
    }
  }
}
