/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.rsf.flow.jsfnav;

import java.util.List;

/** An interface implemented by a ViewProducer indicating that it
 * reports (statically) a set of navigation cases governing the view it
 * renders, as well as producing components for it.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface NavigationCaseReporter {
  /** @return A list of {@link uk.org.ponder.rsf.flow.jsfnav.NavigationCase} 
   * objects specifying the navigation rules to be applied for action returns
   * resulting from handling UICommand triggers for <i>this view</i>, i.e. the
   * view for which this ViewComponentProducer is producing components.
   */
  public List reportNavigationCases();
}
