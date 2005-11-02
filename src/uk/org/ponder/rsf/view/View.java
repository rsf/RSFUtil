/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.view;

import java.util.HashMap;

import uk.org.ponder.rsf.componentprocessor.FormModel;
import uk.org.ponder.rsf.components.UIContainer;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class View {
  public static final String VIEWROOT_ID = "  viewroot  ";
  public View() {
    viewroot = new UIContainer();
    viewroot.ID = VIEWROOT_ID;
  }
  public UIContainer viewroot;
  // This is a map of FULL component IDs to String messages.
  public HashMap IDtomessage = new HashMap();
  public FormModel formmodel;
}
