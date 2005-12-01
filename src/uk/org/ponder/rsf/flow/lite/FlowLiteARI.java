/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.hashutil.EighteenIDGenerator;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** The ActionResultInterpreter in force when a "RSF Flow Lite" is governing
 * the current request sequence. It is only invoked when non-empty contents
 * are discovered in the FlowIDHolder by the FlowLiteARIResolver, and thus
 * ensures that we only receive results from a FlowActionProxyBean, which
 * returns ViewableState objects referring to the next view state to be shown. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class FlowLiteARI implements ActionResultInterpreter {
  // less hassle to keep this here than manage as a dependency....
  private static EighteenIDGenerator idgenerator = new EighteenIDGenerator();
  private ActionResultInterpreter defaultari;

  public void setDefaultARI(ActionResultInterpreter defaultari) {
    this.defaultari = defaultari;
  }
  
  public ARIResult interpretActionResult(ViewParameters incoming, Object result) {
    if (!(result instanceof ViewableState)) {
      return defaultari.interpretActionResult(incoming, result);
    }
    else {
    ARIResult togo = new ARIResult();
    togo.resultingview = incoming.copyBase();
    if (result.equals(ARIResult.FLOW_START)) {
      togo.propagatebeans = ARIResult.FLOW_START;
      togo.resultingview.flowtoken = idgenerator.generateID();
    }
    else {
      // it is an error not to be passed a ViewableState
      ViewableState state = (ViewableState) result;
      
      if (state instanceof EndState) {
        togo.propagatebeans = ARIResult.FLOW_END;
      }
      else { // ViewState
        togo.propagatebeans = ARIResult.PROPAGATE;
      }
    }
    return togo;
  }
  }

}
