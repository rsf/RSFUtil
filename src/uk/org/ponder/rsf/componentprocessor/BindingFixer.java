/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIDeletionBinding;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.components.UIParameterHolder;
import uk.org.ponder.rsf.request.FossilizedConverter;
import uk.org.ponder.rsf.request.SubmittedValueEntry;

/** A fixer which converts deletion and EL bindings to their "raw" form
 * as fossilized key/value pairs.
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class BindingFixer implements ComponentProcessor {

  private FossilizedConverter fossilizedconverter;

  public void setFossilizedConverter(FossilizedConverter fossilizedconverter) {
    this.fossilizedconverter = fossilizedconverter;
  }
  
  public void processComponent(UIComponent toprocess) {
    if (toprocess instanceof UICommand) {
      // add the notation explaining which control is submitting, when it does
      UICommand command = (UICommand) toprocess;
      command.parameters.add(new UIParameter(
          SubmittedValueEntry.SUBMITTING_CONTROL, toprocess.getFullID()));
      if (command.methodbinding != null) {
        command.parameters
            .add(new UIParameter(SubmittedValueEntry.FAST_TRACK_ACTION,
                command.methodbinding.value));
      }
    }
    if (toprocess instanceof UIParameterHolder) {
      processParameterList(((UIParameterHolder)toprocess).parameters);
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
