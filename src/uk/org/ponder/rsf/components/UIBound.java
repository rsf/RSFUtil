/*
 * Created on Aug 9, 2005
 */
package uk.org.ponder.rsf.components;

/** The base class of all input components, which have their values 
 * bound to a bean reference. This is used both on render to give them their
 * initial values, and on submission to apply the input value to the model.
 * <br> Immediate descendents are UIInput(One) and UIInputMany, as well as UIOutput.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class UIBound extends UIComponent {
  /** The EL value reference that this component's value is bound to.
   * This will be a string of the form <code>#{rootbean.property1.property2}</code>
   */
  public String valuebinding;
  /** A field recording whether the value of this binding at render time will
   * be "fossilized", i.e. recorded by the client and resubmitted with modified 
   * values. Defaults to <code>true</code> for input components, and 
   * <code>false</code> for output components.
   * <p>Producers of output components with high consistency requirements (i.e.
   * those that will be used to provide critical values to users during multi-
   * requests should override the constructor default in UIOutput with <code>true</code>.
   */
  public boolean fossilize = false;
}
