/*
 * Created on May 11, 2006
 */
package uk.org.ponder.rsf.components;

/**
 * Represents the site of rendering of a label from a UISelect component.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class UISelectLabel extends UIComponent {
  public String parentFullID;

  public int choiceindex;

  public static UISelectLabel make(UIContainer tofill, String ID,
      String parentFullID, int choiceindex) {
    UISelectLabel togo = new UISelectLabel();
    togo.ID = ID;
    togo.parentFullID = parentFullID;
    togo.choiceindex = choiceindex;
    tofill.addComponent(togo);
    return togo;
  }
}
