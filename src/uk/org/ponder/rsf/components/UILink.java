/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * This component peers with a simple navigational link. Operating this link
 * is expected to give rise to an idempotent request.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class UILink extends UIBoundString {
  /** A string representing the target of this link - e.g. in an HTML system,
   * a URL.
   */
  public String target;
  
  public static UILink make(UIContainer parent, String ID, String text, String target) {
    UILink togo = new UILink();
    togo.ID = ID;
    togo.setValue(text);
    togo.target = target;
    parent.addComponent(togo);
    return togo;
  }
  
   public static UILink make(UIIKATContainer parent, String ID, String target) {
    return make(parent, ID, null, target);
  }

}
