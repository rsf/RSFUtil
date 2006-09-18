/*
 * Created on 17-Sep-2006
 */
package uk.org.ponder.rsf.producers;

import uk.org.ponder.rsf.components.UIContainer;

/** A view producer with no user-exported dependencies. Typically more useful
 * within framework code. */

public interface NullaryProducer {
  public void fillComponents(UIContainer tofill);
}
