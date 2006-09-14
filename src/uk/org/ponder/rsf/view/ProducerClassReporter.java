/*
 * Created on 14 Sep 2006
 */
package uk.org.ponder.rsf.view;

/** Allows a producer to report its "class" or category (corresponding to a
 * "component type" in other framework), e.g. richText, dateInput or the like.
 * This allows a form of "autowiring by type" whereby users need not explicitly
 * configure in the producer for each type of "component", but rather just
 * expose a setter named, for example, <code>setRichTextProducer</code> or
 * <code>setDateInputProducer</code> which accepts an argument conformable
 * to the actual type of "producer" implementing this interface.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface ProducerClassReporter {
  public String getProducerClass();
}
