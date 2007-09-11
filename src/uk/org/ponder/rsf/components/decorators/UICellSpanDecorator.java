/*
 * Created on 11 Sep 2007
 */
package uk.org.ponder.rsf.components.decorators;

/**
 *  Determines the number of tabular cells that this component occupies. In
 * HTML, will render attributes <code>colspan</code> and <code>rowspan</code>
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class UICellSpanDecorator implements UIDecorator {
  public static final int UNSET_VALUE = -1;
  public int colspan = UNSET_VALUE;
  public int rowspan = UNSET_VALUE;
  public UICellSpanDecorator() {}
  public UICellSpanDecorator(int colspan, int rowspan) {
    this.colspan = colspan; this.rowspan = rowspan;
  }
}
