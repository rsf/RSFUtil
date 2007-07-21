/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.flow.errors;

import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** The basic ViewExceptionStrategy which redirects to the determined default
 * view in the case of any error. By default, this is mapped to the last
 * position in the list of strategies and will handle all cases not
 * handled by earlier registrations.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class DefaultViewExceptionStrategy implements ViewExceptionStrategy {
  private AnyViewParameters defaultview;
  public void setDefaultView(ViewParameters defaultview) {
    this.defaultview = defaultview;
  }
  public AnyViewParameters handleException(Exception e, ViewParameters incoming) {
    return defaultview.get();
  }
  
}
