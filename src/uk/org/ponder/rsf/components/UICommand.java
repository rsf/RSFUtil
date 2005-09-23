/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.Map;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UICommand extends UIOutput {
  // a map of param/values to be surreptitiously added to the parameter
  // map during submission. We maintain these as single-valued here for
  // simplicity, but they will be upgraded to the standard String[] values
  // on delivery.
  // the action binding is simply added here as another key of FAST_TRACK_ACTION.
  public Map commandparams;
}
