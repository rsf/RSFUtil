/*
 * Created on 17-Sep-2006
 */
package uk.org.ponder.rsf.producers;

import uk.org.ponder.rsf.components.UIContainer;

/** A basic "producer" with no specific seed component. Hence accepts a 
 * 2nd argument indicating the desired client ID. This is suitable, 
 * for example, for accepting the page body from a main ViewProducer.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface BasicProducer {
  public void fillComponents(UIContainer parent, String clientID);
}
