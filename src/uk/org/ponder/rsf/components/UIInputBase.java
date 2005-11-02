/*
 * Created on Aug 11, 2005
 */
package uk.org.ponder.rsf.components;

/** The base class of all single-valued input components. No members appear
 * here since the value objects descended from here have different types.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class UIInputBase extends UIBound {
  public UIInputBase() {
    fossilize = true;
  }
}
