/*
 * Created on 30 Nov 2006
 */
package uk.org.ponder.rsf.evolvers;

import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UISelect;

public class PlainSelectEvolver implements SelectEvolver {
  public static final String COMPONENT_ID = "plainSelect:";
  public UIJointContainer evolveSelect(UISelect toevolve) {
    UIJointContainer joint = new UIJointContainer(toevolve.parent,
        toevolve.ID, COMPONENT_ID);
    toevolve.parent.remove(toevolve);
    toevolve.ID = "select";
    joint.addComponent(toevolve);
    return joint;
  }
}
