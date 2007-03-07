/*
 * Created on 7 Mar 2007
 */
package uk.org.ponder.rsf.components;

/** Tag node representing that the rendering behaviour of this UIListMember 
 * is expected to be identical with UIBound. The only current 
 * non-UIBasicListMember is UISelectChoice */

public class UIBasicListMember extends UIListMember {
  public static UIBasicListMember makeBasic(UIContainer tofill, String ID,
      String parentFullID, int choiceindex) {
    UIBasicListMember togo = new UIBasicListMember();
    togo.ID = ID;
    togo.parentFullID = parentFullID;
    togo.choiceindex = choiceindex;
    tofill.addComponent(togo);
    return togo;
  }
}
