/*
 * Created on 10 Jan 2008
 */
package uk.org.ponder.rsf.bare;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.rsf.flow.ARIResult;

/** A record summarising the response from a POST operation, consisting of the
 * final state of the request context, and the ARIResult entry.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class ActionResponse {
  public BeanLocator requestContext;
  public ARIResult ARIResult;
}
