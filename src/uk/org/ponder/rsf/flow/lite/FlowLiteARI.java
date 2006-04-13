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
// NB, this and FLARIR are currently DISUSED since all logic is performed by
// the proxy bean. The problem we are currently facing is this somewhat 
// assymetry, caused by errors which are triggered during VALUE DECODING, which
// is a time out of scope of this flow logic.

// issues: we would like orthogonality in how these errors are treated by the
// user in staticerrorstrategies, but there seems to be a real mismatch of
// idioms. For a start, the proxy bean may NEVER BE INVOKED if there is a
// decode exception. And secondly, WHICH TRANSITION is expected to have
// generated the error? We assume that the error return from the AES is
// propagated "forward in time" to the first CONCRETE state transition that
// the flow manages, and replaces it. But the incoming action invocation is
// targeted at the FLOW PROXY, and not at the backing CONCRETE bean, how can
// we sort this out without breaking someone's encapsulation?

// This HAS to happen. But how can we insist that the flow ALWAYS starts even
// if action decode errors occur? This is crazy. General users will NOT want
// to be queried in the case of incorrect submissions, whereas unless the
// FlowProxyBean starts up it cannot POSSIBLY have the result code of an 
// exception processor injected into it. This suggests that the Proxy model
// for SWF is fundamentally incorrect. Instead of being the ACTION, perhaps
// the operator should actually be the ARI??! Well no, then how will the user
// express what the target of the operation is. This suggests we need to 
// "bust open" the proxy model and put more intelligence in the ActionHandler.
// It can do a "getBean" for the action, and then based on what it finds, 
// blow some sense into it about i) whether an result has "already occurred"
// and then invoke the "handle()" method on it manually. 

// if there are further internal actions of course, the decode does not occurs...
// the result seems to be to recommend that users of Flow Proxy beans do type
// conversion by themselves, or else adopt the awkward strategy in the AES of
// FULLY RESOLVING the ARIResult for the error case.

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
