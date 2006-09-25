/*
 * Created on 21 Sep 2006
 */
package uk.org.ponder.rsf.evolvers;

import java.util.Date;

import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;

public interface DateInputEvolver {
  public UIJointContainer evolveDateInput(UIInput toevolve, Date value);
}
