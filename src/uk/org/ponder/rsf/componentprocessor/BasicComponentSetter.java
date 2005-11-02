/*
 * Created on Aug 7, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInputMany;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelectBoolean;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class BasicComponentSetter implements ComponentSetter {

  // value is either String or String[]
  public void setValue(UIComponent toset, Object value) {
    if (toset instanceof UIInput) {
      ((UIInput)toset).value = (String)value;
    }
    else if (toset instanceof UIOutput) {
      ((UIOutput)toset).text = (String)value;
    }
    else if (toset instanceof UISelectBoolean) {
      ((UISelectBoolean)toset).value = value.equals("true");
    }
    else if (toset instanceof UIInputMany) {
      ((UIInputMany)toset).values = (String[]) value;
    }
  }

}
