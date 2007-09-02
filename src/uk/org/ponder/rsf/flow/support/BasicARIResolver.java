/*
 * Created on Nov 30, 2005
 */
package uk.org.ponder.rsf.flow.support;

import uk.org.ponder.rsf.flow.ARIResolver;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;

/** A basic implemenation of the ARIResolver interface which simply defers
 * to a particular injected bean.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class BasicARIResolver implements ARIResolver {
  private ActionResultInterpreter ari;
  public void setActionResultInterpreter(ActionResultInterpreter ari) {
    this.ari = ari;
  }
  public ActionResultInterpreter getActionResultInterpreter() {
    return ari;
  }

}
