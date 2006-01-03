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
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class UIInternalLink extends UILink {
  public static UIInternalLink make(UIContainer parent, String ID, String text, 
      ViewParameters viewparams) {
    UIInternalLink togo = new UIInternalLink();
    togo.ID = ID;
    togo.linktext = text;
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
