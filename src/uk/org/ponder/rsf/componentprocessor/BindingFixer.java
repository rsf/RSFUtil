/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIDeletionBinding;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.state.FossilizedConverter;

public class BindingFixer implements ComponentProcessor {

  private FossilizedConverter fossilizedconverter;

  public void setFossilizedConverter(FossilizedConverter fossilizedconverter) {
    this.fossilizedconverter = fossilizedconverter;
  }
  
  public void processComponent(UIComponent toprocess) {
    if (toprocess instanceof UIForm) {
      processParameterList(((UIForm)toprocess).parameters);
    }
    else if (toprocess instanceof UICommand) {
      processParameterList(((UICommand)toprocess).parameters);
    }
  }

  private void processParameterList(ParameterList list) {
    for (int i = 0; i < list.size(); ++ i) {
      UIParameter param = list.parameterAt(i);
      if (param instanceof UIDeletionBinding) {
        fossilizedconverter.computeDeletionBinding((UIDeletionBinding) param);
      }
      else if (param instanceof UIELBinding) {
        fossilizedconverter.computeELBinding((UIELBinding) param);
      }
    }
  }

}
