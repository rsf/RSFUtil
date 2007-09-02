/*
 * Created on Oct 8, 2005
 */
package uk.org.ponder.rsf.flow.support;

import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** An action result interpreter that simply redirects to the incoming
 * view.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class DefaultActionResultInterpreter implements ActionResultInterpreter {

  public ARIResult interpretActionResult(ViewParameters incoming, Object result) {
    ARIResult togo = new ARIResult();
    togo.resultingView = incoming.copyBase();
    togo.propagateBeans = ARIResult.FLOW_END;
    return togo;
  }
  
}
