/*
 * Created on 23 Sep 2006
 */
package uk.org.ponder.rsf.evolvers.support;

import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;

/** A plain implementation of a TextInputEvolver, that will simply upgrade
 * the supplied UIInput into a Joint named "plainTextEditor:", giving it 
 * the ID "textarea".
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class PlainTextInputEvolver implements TextInputEvolver {
  public static final String COMPONENT_ID = "plainTextEditor:";
  public UIJointContainer evolveTextInput(UIInput toevolve) {
    UIJointContainer joint = new UIJointContainer(toevolve.parent,
        toevolve.ID, COMPONENT_ID);
    toevolve.parent.remove(toevolve);
    toevolve.ID = SEED_ID;
    joint.addComponent(toevolve);
    return joint;
  }
}
