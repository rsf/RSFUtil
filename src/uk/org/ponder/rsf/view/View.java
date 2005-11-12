/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.view;

import java.util.HashMap;

import uk.org.ponder.rsf.componentprocessor.FormModel;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIIKATContainer;

/**
 * The View holds the component tree while it is in transit between 
 * component producers and the view renderer. The tree is built up by 
 * accreting components from one or more producers, and then passes through
 * various "fixups" (ComponentProcessors) before it is handed to the IKAT
 * renderer implemented in ViewRender.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class View {
  public static final String VIEWROOT_ID = "  viewroot  ";
  public View() {
    viewroot = new UIIKATContainer();
    viewroot.ID = VIEWROOT_ID;
  }
  public UIIKATContainer viewroot;
  // This is a map of FULL component IDs to String messages.
  public HashMap IDtomessage = new HashMap();
  // This is a map of full component IDs to components.
  private HashMap IDtocomponent = new HashMap();
  /** Registers the supplied component into the ID map */
  public void registerComponent(UIComponent toregister) {
    IDtocomponent.put(toregister.getFullID(), toregister);
  }
  public UIComponent getComponent(String fullID) {
    return (UIComponent) IDtocomponent.get(fullID);
  }
  public FormModel formmodel;
}
