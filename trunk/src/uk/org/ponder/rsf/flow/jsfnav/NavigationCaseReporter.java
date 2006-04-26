/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.rsf.flow.jsfnav;

import java.util.List;

/** An interface implemented by a ComponentProducer indicating that it
 * reports (statically) a set of navigation cases governing the view it
 * renders, as well as producing components for it.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface NavigationCaseReporter {
  public List reportNavigationCases();
}
