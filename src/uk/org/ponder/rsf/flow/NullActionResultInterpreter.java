/*
 * Created on Oct 8, 2005
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.webapputil.ViewParameters;

/** An action result interpreter that simply redirects to the incoming
 * view.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class NullActionResultInterpreter implements ActionResultInterpreter {

  public ARIResult interpretActionResult(ViewParameters incoming, String result) {
    ARIResult togo = new ARIResult();
    togo.resultingview = incoming; 
    togo.propagatebeans = ThreadErrorState.isError()? ARIResult.PROPAGATE_ERROR_ONLY:
      ARIResult.DESTROY;
    return togo;
  }
  
}
