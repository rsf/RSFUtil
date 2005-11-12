/*
 * Created on Nov 2, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.rsf.componentprocessor.ComponentProcessor;
import uk.org.ponder.rsf.components.ComponentList;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIIKATContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.util.RSFUtil;

/** A fixer to be run BEFORE the main form fixer, which implements the HTML/HTTP
 * form model whereby ALL nested child controls of the form are submitted. 
 * This fixer will perform no action if it discovers that the "submittingcontrols"
 * fields of {@see uk.org.ponder.rsf.components.UIForm} has already been filled in.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class ContainmentFormChildFixer implements ComponentProcessor {

  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIForm) {
      UIForm toprocess = (UIForm) toprocesso;
      registerContainer(toprocess, toprocess);
    }
  }
  
  private void registerContainer(UIForm toprocess, UIContainer toregister) {
    ComponentList children = toregister.flattenChildren();
    for (int i = 0; i < children.size(); ++ i) {
      UIComponent child = children.componentAt(i);
      if (RSFUtil.isBound(child)) {
        toprocess.submittingcontrols.add(child.getFullID());
      }
      else if (child instanceof UIIKATContainer) {
        registerContainer(toprocess, (UIIKATContainer) child);
      }
    }
  }
}
