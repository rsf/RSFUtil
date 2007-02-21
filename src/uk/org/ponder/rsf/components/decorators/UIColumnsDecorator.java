/*
 * Created on 20 Feb 2007
 */
package uk.org.ponder.rsf.components.decorators;

/** Determines the visible width, measured in text columns, of a text control **/

public class UIColumnsDecorator extends UITextDimensionsDecorator {
  public UIColumnsDecorator(int columns) {
    this.columns = columns;
  }
}
