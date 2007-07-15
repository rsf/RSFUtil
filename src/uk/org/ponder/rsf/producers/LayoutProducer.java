/*
 * Created on 12 Jul 2007
 */
package uk.org.ponder.rsf.producers;

/** A signature suitable for a producer implementing an exterior "layout" - 
 * see http://www2.caret.cam.ac.uk/rsfwiki/Wiki.jsp?page=OuterPageTemplates.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface LayoutProducer extends NullaryProducer {
  /** Receive the producer from the framework which will deliver the 
   * page-specific component tree */
  
  public void setPageProducer(NullaryProducer pageproducer);
}
