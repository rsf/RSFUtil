/*
 * Created on 11 Sep 2007
 */
package uk.org.ponder.rsf.components.decorators;

/**
 * Determines the number of vertical tabular cells that this component occupies.
 * In HTML, will render attribute <code>colspan</code>.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class UIColSpanDecorator extends UICellSpanDecorator {
  public UIColSpanDecorator(int colspan) {
    this.colspan = colspan;
  }
}
