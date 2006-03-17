/*
 * Created on Nov 25, 2005
 */
package uk.org.ponder.rsf.flow.lite;

import uk.org.ponder.rsf.flow.ARIResolver;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;

/** Resolves the correct ActionResultInterpreter to be used for this
 * request. Returns either the FlowLiteARI if the FlowIDHolder is found nonempty,
 * or a default ARI.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class FlowLiteARIResolver implements ARIResolver {
  private ActionResultInterpreter defaultari;
  private FlowIDHolder flowidholder;
  private FlowLiteARI flowari;
  public void setDefaultARI(ActionResultInterpreter defaultari) {
    this.defaultari = defaultari;
  }
  public void setFlowLiteARI(FlowLiteARI flowari) {
    this.flowari = flowari;
  }
  public void setFlowIDHolder(FlowIDHolder flowidholder) {
    this.flowidholder = flowidholder;
  }
  public ActionResultInterpreter getActionResultInterpreter() {
    if (flowidholder.getFlowID() != null) {
      return flowari;
    }
    else {
      return defaultari;
    }
  }
}
