/*
 * Created on Oct 8, 2005
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** An action result interpreter that simply redirects to the incoming
 * view.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class NullActionResultInterpreter implements ActionResultInterpreter {

  public ARIResult interpretActionResult(ViewParameters incoming, Object result) {
    ARIResult togo = new ARIResult();
    togo.resultingview = incoming; 
    togo.propagatebeans = ARIResult.FLOW_END;
    return togo;
  }
  
}
