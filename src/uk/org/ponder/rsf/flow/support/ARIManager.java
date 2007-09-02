/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsf.flow.support;

import java.util.List;

import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** An ARIResolver that will accept a list of primary resolvers which it
 * will poll in order. The first resolver that does not return <code>>null</code>
 * will have its result accepted.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class ARIManager implements ActionResultInterpreter {

  private List arilist;

  public void setARIList(List arilist) {
    this.arilist = arilist;
  } 
  
  public ARIResult interpretActionResult(ViewParameters incoming, Object result) {
    for (int i = 0; i < arilist.size(); ++ i) {
      ActionResultInterpreter ari = (ActionResultInterpreter) arilist.get(i);
      ARIResult togo = ari.interpretActionResult(incoming, result);
      if (togo != null) return togo;
    }
    return null;
  }

}
