/*
 * Created on 6 Feb 2008
 */
package uk.org.ponder.rsf.bare;

import uk.org.ponder.beanutil.BeanLocator;

/** The base class for the response type of a "test invocation" of an RSF
 * request cycle. Includes only a field indicating the "final state" of the
 * request context.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class RequestResponse {
  /** A BeanLocator holding the final state of the request context. **/
  public BeanLocator requestContext;
}
