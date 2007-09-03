/*
 * Created on 1 Aug 2007
 */
package uk.org.ponder.rsf.components;

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
}
