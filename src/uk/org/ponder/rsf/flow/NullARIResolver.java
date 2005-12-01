/*
 * Created on Nov 30, 2005
 */
package uk.org.ponder.rsf.flow;

public class NullARIResolver implements ARIResolver {
  private ActionResultInterpreter ari;
  public void setActionResultInterpreter(ActionResultInterpreter ari) {
    this.ari = ari;
  }
  public ActionResultInterpreter getActionResultInterpreter() {
    return ari;
  }

}
