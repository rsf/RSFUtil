/*
 * Created on 20 Feb 2007
 */
package uk.org.ponder.rsf.components.decorators;

/** Determines the dimensions, measured in characters, of a text control **/

public class UITextDimensionsDecorator implements UIDecorator {
  public static final int UNSET_VALUE = -1;
  public int columns = UNSET_VALUE;
  public int rows = UNSET_VALUE;
  public UITextDimensionsDecorator() {}
  public UITextDimensionsDecorator(int columns, int rows) {
    this.columns = columns; this.rows = rows;
  }
}
