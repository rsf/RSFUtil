/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.Map;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UICommand extends UIComponent {
  //public String actionhandler;
  public String text;
  // a map of param/values to be surreptitiously added to the parameter
  // map during submission. We maintain these as single-valued here for
  // simplicity, but they will be upgraded to the standard String[] values
  // on delivery.
  public Map commandparams;
}
