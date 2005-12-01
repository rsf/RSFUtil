/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * This component peers with a simple navigational link. Operating this link
 * is expected to give rise to an idempotent request.
 * The basic case where the command is rendered using a piece of
 * non-bound text is handled by filling in the "linktext" field. For more
 * complex command contents including bound ones, leave this field as null 
 * and add rendering components as childen of the link.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class UILink extends UISimpleContainer {
  /** A string representing the target of this link - e.g. in an HTML system,
   * a URL.
   */
  public String target;
  
  public String linktext;
  
  public static UILink make(UIContainer parent, String ID, String text, String target) {
    UILink togo = new UILink();
    togo.ID = ID;
    togo.target = target;
    togo.linktext = text;
    parent.addComponent(togo);    
    return togo;
  }
  
   public static UILink make(UIBranchContainer parent, String ID, String target) {
    return make(parent, ID, null, target);
  }

}