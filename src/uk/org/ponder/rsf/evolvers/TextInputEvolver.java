/*
 * Created on 22 Sep 2006
 */
package uk.org.ponder.rsf.evolvers;

import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;

public interface TextInputEvolver {
  public UIJointContainer evolveTextInput(UIInput toevolve);
}
