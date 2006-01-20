/*
 * Created on Jan 20, 2006
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.stringutil.StringList;

/** A UIError component has no visual function, but is used to bind a 
 * particular visual component to a set of bean paths from which exceptions
 * will be collected to populate an error list.  
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class UIError extends UIComponent {
  
  /** A list of paths from which exceptions resulting from READS (getXxxx)will be 
   * collected.
   */
  public StringList outputbinding;
  /** A list of paths from which exceptions resulting from WRITES (setXxxx) will be 
   * collected.
   */
  public StringList inputbinding;
}
