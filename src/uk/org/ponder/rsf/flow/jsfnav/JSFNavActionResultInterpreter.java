/*
 * Created on 10-Feb-2006
 */
package uk.org.ponder.rsf.flow.jsfnav;

import java.util.List;
import java.util.Map;

import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
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

  public void setNavigationMap(NavigationMap map) {
    this.map = map;
  }

  public void setNavigationCasePooler(NavigationCasePooler pooler) {
    fromviews = pooler.getPooledMap();
  }

  private static void processCaseList(List caselist, ARIResult togo,
      Object result) {
    if (caselist != null) {
      for (int j = 0; j < caselist.size(); ++j) {
        NavigationCase navcase = (NavigationCase) caselist.get(j);
        // TODO: Fix up these rules wrt. specificity of overrides to agree
        // more with JSF semantics
        if (navcase.fromOutcome == null || navcase.fromOutcome.equals(result)) {
          togo.resultingview = navcase.toViewId;
        }
      }
    }
  }

  public ARIResult interpretActionResult(ViewParameters incoming, Object result) {
    ARIResult togo = new ARIResult();
    togo.resultingview = incoming;
    togo.propagatebeans = ARIResult.FLOW_END;
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
    return matchingrule? togo : null;
  }

}
