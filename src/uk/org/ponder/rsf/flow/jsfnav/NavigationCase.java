/*
 * Created on 10-Feb-2006
 */
package uk.org.ponder.rsf.flow.jsfnav;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Summarises a single "navigation case", a static rule mapping from the
 * return value of an RSF Action into the ViewParameters to be navigated to.
 * Used by the "JSF Navigation Style" ActionResultInterpreter,
 * {@link uk.org.ponder.rsf.flow.jsfnav.JSFNavActionResultInterpreter}.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class NavigationCase {
  public NavigationCase() {}
  public NavigationCase(String fromoutcome, ViewParameters toViewId) {
    this.fromOutcome = fromoutcome;
    this.toViewId = toViewId;
  }
  public String fromOutcome;
  public ViewParameters toViewId;
}
