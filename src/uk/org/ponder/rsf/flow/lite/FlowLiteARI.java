/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.hashutil.EighteenIDGenerator;
import uk.org.ponder.reflect.MethodInvokingProxy;
import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.Logger;

public class FlowLiteARI implements ActionResultInterpreter {

  // less hassle to keep this here than manage as a dependency....
  private static EighteenIDGenerator idgenerator = new EighteenIDGenerator();

  public ARIResult interpretActionResult(ViewParameters incoming, String result) {
    ARIResult togo = new ARIResult();
    togo.resultingview = incoming.copyBase();
    if (result.equals(ARIResult.FLOW_START)) {
      togo.propagatebeans = ARIResult.FLOW_START;
      togo.resultingview.flowtoken = idgenerator.generateID();
    }
    else {
      // it is an error not to be passed a ViewableState
      ViewableState state = (ViewableState) flow.stateFor(result);
      togo.resultingview.viewID = state.viewID;
      
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
