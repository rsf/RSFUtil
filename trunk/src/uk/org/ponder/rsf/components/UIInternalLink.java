/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** A specialisation of a {@link UILink} which specifies a navigation target which is part
 * of the same RSF application. In this case, specification of the target can be done in the
 * abstract, by way of a ViewParameters object, allowing "links to self" to be transparently
 * resolved correctly in whatever the hosting environment is for the current request (servlet,
 * portlet, etc.). The <code>target</code> field will be filled in by a fixup prior to 
 * rendering.
 * <p>Note that the ViewParams object supplied as argument to the "make" methods
 * will be copied prior to being added.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class UIInternalLink extends UILink {
  public static UIInternalLink make(UIContainer parent, String ID, String text, 
      ViewParameters viewparams) {
    UIInternalLink togo = new UIInternalLink();
    togo.ID = ID;
    if (text != null) {
      togo.linktext = new UIOutput();
      togo.linktext.setValue(text);
    }
    togo.viewparams = viewparams.copyBase();
    parent.addComponent(togo);
    return togo;
  }
  
  public static UIInternalLink make(UIContainer parent, String ID, ViewParameters viewparams) {
    UIInternalLink togo = new UIInternalLink();
    togo.ID = ID;
    togo.viewparams = viewparams.copyBase();
    parent.addComponent(togo);
    return togo;
  }
  
  public ViewParameters viewparams;
}
