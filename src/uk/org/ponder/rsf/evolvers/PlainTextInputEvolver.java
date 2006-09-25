/*
 * Created on 23 Sep 2006
 */
package uk.org.ponder.rsf.evolvers;

import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;

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
        COMPONENT_ID, toevolve.ID);
    toevolve.parent.move(toevolve, joint);
    toevolve.ID = "textarea";
    return joint;
  }
}
