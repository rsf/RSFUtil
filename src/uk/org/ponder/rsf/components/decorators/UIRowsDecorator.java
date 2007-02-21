/*
 * Created on 20 Feb 2007
 */
package uk.org.ponder.rsf.components.decorators;

/** Determines the visible height, measured in text rows, of a text control **/

public class UIRowsDecorator extends UITextDimensionsDecorator {
  public UIRowsDecorator(int rows) {
    this.rows = rows;
  }
}
