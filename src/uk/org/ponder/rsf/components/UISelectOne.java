/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.stringutil.StringList;

/**
 * Represents a "drop-down" list of options from which the user may choose.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UISelectOne extends UIInput {
  public UISelectItem[] choices;
  

  public static UISelectOne make(UIContainer parent, String ID, StringList values,
      String selected, String valuebinding) {
    UISelectOne togo = new UISelectOne();
    togo.ID = ID;
    togo.value = selected;
    togo.choices = new UISelectItem[values.size()];

    for (int i = 0; i < values.size(); ++i) {
      String value = values.stringAt(i);
      togo.choices[i] = new UISelectItem(value, value);
    }
    togo.valuebinding = valuebinding;
    //RSFUtil.setFossilisedBinding(parent, togo, valuebinding, selected);
    return togo;
  }

}
