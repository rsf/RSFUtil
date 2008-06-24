/*
 * Created on 1 Aug 2007
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.util.RSFUtil;

public class UIParameterHolder extends UIComponent {
  /** A list of key/value pairs which should represent EL bindings
   * ({@link UIELBinding}). Note that the bindings which are added to a
   * UICommand's list will ONLY be submitted if the command is the submitting
   * control.
   */
  public ParameterList parameters = new ParameterList();
  
  /** Add the supplied parameter to the parameter list stored */
  public UIParameterHolder addParameter(UIParameter param) {
    parameters.add(param);
    return this;
  }
  
  /** Adds a "deferred resulting view" EL binding to this component.
   * This will transfer a value from an EL path in the request context to a
   * particular path withing the outgoing ViewParameters state for the coming 
   * action cycle, assuming that it completes without error.
   * @param viewParamsPath The path within the outgoing ViewParameters state for the
   * navigation which is to receive the deferred value.
   * @param requestPath The path within the "final state" of the request context at the
   * end of the cycle from which the deferred value is to be read.
   */
  
  public void addResultingViewBinding(String viewParamsPath, String requestPath) {
    RSFUtil.addResultingViewBinding(this, viewParamsPath, requestPath);
  }
  
  /** Adds a "literal" resulting view binding to this control. Rather than reading
   * a path in the final request context as in {@link #addResultingViewBinding(String, String)}, 
   * this supplies a constant, literal value into the outgoing state.
   * @param viewParamsPath The path within the outgoing ViewParameters state for the
   * navigation which is to receive the deferred value.
   * @param value The value to be applied to the outgoing ViewParameters path.
   */ 
  public void addResultingViewBinding(String viewParamsPath, Object value) {
    RSFUtil.addResultingViewBinding(this, viewParamsPath, value);
  }
 
}
