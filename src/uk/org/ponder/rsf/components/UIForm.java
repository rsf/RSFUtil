/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIForm extends UIComponent {
  /** The URL to which this form will be submitted. This SHOULD be the same
   * as the URL of the page containing the form, minus query parameters.
   */
  public String postURL;
  public Map hiddenfields = new HashMap();
}
