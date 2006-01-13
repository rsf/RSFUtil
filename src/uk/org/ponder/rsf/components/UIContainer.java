/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.components;

public abstract class UIContainer extends UIComponent {

  public abstract void addComponent(UIComponent toadd);
  public abstract ComponentList flattenChildren();
  
  /** A list of key/value pairs which should represent EL bindings
   * ({@link UIELBinding}). Note that the bindings which are added to a
   * UICommand's list will ONLY be submitted if the command is the submitting
   * control.
   */
  //TODO: mapping!!!
  public ParameterList parameters = new ParameterList();
}
