/*
 * Created on 6 Feb 2008
 */
package uk.org.ponder.rsf.bare;

import uk.org.ponder.rsf.components.UIComponent;


/** A class encapsulating a query over the component tree, most useful for
 * automated testing situations. This is a simple Query By Example (QBE) system,
 * where the filled-in fields in a supplied "exemplar" are used to construct
 * a query by AND-ing their specifications. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class ComponentQuery {
  
  public UIComponent exemplar;
  
  public ComponentQuery(UIComponent exemplar) {
    this.exemplar = exemplar;
  }
  
  
}
