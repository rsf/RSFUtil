/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * A specialisation of a {@link UILink} which specifies a navigation target
 * which is part of the same RSF application. In this case, specification of the
 * target can be done in the abstract, by way of a ViewParameters object,
 * allowing "links to self" to be transparently resolved correctly in whatever
 * the hosting environment is for the current request (servlet, portlet, etc.).
 * The <code>target</code> field will be filled in by a fixup prior to
 * rendering.
 * <p>
 * Note that the {@link ViewParameters} object supplied as argument to the "make" methods
 * will be copied prior to being added.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */

public class UIInternalLink extends UILink {
  /**
   * @param parent The parent container to which this link is to be added
   * @param ID The (RSF) ID to be given to this linke
   * @param linktext The text which is to be attached to this link (if left <code>null</code>, 
   * the tag peering with this component may contain further markup in the template)
   * @param viewparams The {@link ViewParameters} specifying the target view for this link. 
   * @return The constructed internal link
   */
  
  public static UIInternalLink make(UIContainer parent, String ID, 
      UIBoundString linktext, ViewParameters viewparams) {
    UIInternalLink togo = new UIInternalLink();
    togo.ID = ID;
    togo.linktext = linktext;
    togo.viewparams = viewparams.copyBase();
    parent.addComponent(togo);
    return togo;  
  }
  
  /** {@see UIInternalLink#make(UIContainer, String, UIBoundString, ViewParameters) */
  
  public static UIInternalLink make(UIContainer parent, String ID, String text,
      ViewParameters viewparams) {
    UIBoundString linktext = null;
    if (text != null) {
      linktext = new UIOutput();
      linktext.setValue(text);
    }
    return make(parent, ID, linktext, viewparams);
  }

  /** {@see UIInternalLink#make(UIContainer, String, UIBoundString, ViewParameters) */
  
  public static UIInternalLink make(UIContainer parent, String ID,
      ViewParameters viewparams) {
    return make(parent, ID, (UIBoundString)null, viewparams);
  }

  /** Create a link which, while internal, does not participate in the
   * ViewParameters system. This is largely useful for unmanaged environments
   * such as SpringMVC/Cocoon. Cannot be named "make" to avoid clashing with
   * UILink through ridiculous Java semantics on static methods.
   */
  public static UIInternalLink makeURL(UIContainer parent, String ID,
      String target) {
    UIInternalLink togo = new UIInternalLink();
    togo.ID = ID;
    togo.target = new UIOutput();
    if (target != null) {
      togo.target.setValue(target);
    }
    parent.addComponent(togo);
    return togo;
  }
  /** ViewParameters representing the navigation target of this link control */
  public ViewParameters viewparams;
}
