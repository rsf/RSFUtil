/*
 * Created on 11 Sep 2007
 */
package uk.org.ponder.rsf.components.decorators;

/**
 * Determines the number of horizontal tabular cells that this component occupies. In
 * HTML, will render attribute <code>rowspan</code>.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIRowSpanDecorator extends UICellSpanDecorator {
  public UIRowSpanDecorator(int rowspan) {
    this.rowspan = rowspan;
  }
}
