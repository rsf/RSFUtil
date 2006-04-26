/*
 * Created on Apr 26, 2006
 */
package uk.org.ponder.rsf.components;

/** A component which allows raw markup to be inserted into the rendered
 * output. Any users of this class should be flayed alive and then burnt over
 * hot coals. You have been warned - you have stepped outside the bounds of 
 * RSF and the portability and coherence of your app cannot be guaranteed!
 * @author Martin G. Bananas (antranig@caret.cam.ac.uk)
 */

public class UIVerbatim extends UIComponent {
  /** An Object holding the raw markup to be rendered. Either a String,
   * InputStream (assumed UTF-8) or Reader, else toString() will be called 
   * to get character data.
   */
  public Object markup;
}
