/*
 * Created on 17-Sep-2006
 */
package uk.org.ponder.rsf.producers.support;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.producers.BasicProducer;
import uk.org.ponder.rsf.producers.NullaryProducer;

/** An "adaptor" processor that accepts the NullaryProducer for the
 * accumulated views in the container, and adapts it to a PageProducer
 * suitable for injection into a client producer (probably a root page
 * producer implementing BasicProducer)
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */
public class PageProducer implements BasicProducer {
  private NullaryProducer collectedProducer;
  public void setCollectedProducer(NullaryProducer collectedProducer) {
    this.collectedProducer = collectedProducer;
  }
  public void fillComponents(UIContainer parent, UIJointContainer clientname) {
    clientname.jointID = "page:";
    collectedProducer.fillComponents(parent);
  }

}
