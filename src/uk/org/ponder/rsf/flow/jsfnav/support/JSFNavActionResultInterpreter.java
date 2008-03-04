/*
 * Created on 10-Feb-2006
 */
package uk.org.ponder.rsf.flow.jsfnav.support;

import java.util.List;
import java.util.Map;

import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** An ActionResultInterpreter implementing "JSF-style" navigation semantics.
 * Encodes a one-step (contextless) rule mapping action result to resulting
 * view, by matching one of a set of "NavigationRules" which may map on
 * String result and/or source view. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class JSFNavActionResultInterpreter implements ActionResultInterpreter {

  private NavigationMap map;

  private Map fromviews;

  private TargettedMessageList messages;

  public void setNavigationMap(NavigationMap map) {
    this.map = map;
  }

  public void setNavigationCasePooler(NavigationCasePooler pooler) {
    fromviews = pooler.getPooledMap();
  }

  public void setTargettedMessageList(TargettedMessageList messages) {
    this.messages = messages;
  }
  
  private static void matchCase(ARIResult togo, NavigationCase navcase) {
    if (navcase.resultingView != null) {
      togo.resultingView = navcase.resultingView.copy();
    }
    togo.propagateBeans = navcase.flowCondition;
  }
  
  private static void processCaseList(List caselist, ARIResult togo,
      Object result) {
    if (caselist != null) {
      NavigationCase defaultcase = null;
      for (int j = 0; j < caselist.size(); ++j) {
        NavigationCase navcase = (NavigationCase) caselist.get(j);
        if (navcase.fromOutcome == null) {
          if (defaultcase == null) {
            defaultcase = navcase;
          }
        }
        else if (navcase.fromOutcome.equals(result)) {
          matchCase(togo, navcase);
          return;
        }
      }
      if (defaultcase != null) {
        matchCase(togo, defaultcase);
      }
    }
  }



  public ARIResult interpretActionResult(ViewParameters incoming, Object result) {
    ARIResult togo = new ARIResult();
    togo.resultingView = incoming;
    togo.propagateBeans = ARIResult.FLOW_END;

    boolean matchingrule = false;
    
    if (map.navigationRules != null) {
      for (int i = 0; i < map.navigationRules.size(); ++i) {
        NavigationRule rule = (NavigationRule) map.navigationRules.get(i);
        if (rule.fromViewId.viewID.equals(incoming.viewID)) {
          matchingrule = true;
          processCaseList(rule.navigationCases, togo, result);
        }
      }
    }
    List rulesfromviews = (List) fromviews.get(incoming.viewID);
    if (rulesfromviews != null && rulesfromviews.size() > 0) {
      matchingrule = true;
    }
    processCaseList(rulesfromviews, togo, result);
    if (messages.isError()) {
      // Apply this default EARLY so that a following ARI2 can see our decision
      // However, we *do* need to pick up any flow condition from the desired
      // NavigationCase. In fact we probably need a separate "onError" form of
      // NavigationCase "fromOutcome".
      togo.resultingView = incoming.copyBase();
    }
    return matchingrule? togo : null;
  }

}
