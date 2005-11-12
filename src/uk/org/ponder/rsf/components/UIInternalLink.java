/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.viewstate.ViewParameters;

public class UIInternalLink extends UILink {
  public static UIInternalLink make(UIContainer parent, String ID, String text, 
      ViewParameters viewparams) {
    UIInternalLink togo = new UIInternalLink();
    togo.ID = ID;
    togo.setValue(text);
    togo.viewparams = viewparams;
    parent.addComponent(togo);
    return togo;
  }
  
  public static UIInternalLink make(UIContainer parent, String ID, ViewParameters viewparams) {
    UIInternalLink togo = new UIInternalLink();
    togo.ID = ID;
    togo.viewparams = viewparams;
    parent.addComponent(togo);
    return togo;
  }
  
  public ViewParameters viewparams;
}
