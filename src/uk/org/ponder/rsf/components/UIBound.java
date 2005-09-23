/*
 * Created on Aug 9, 2005
 */
package uk.org.ponder.rsf.components;

/** The base class of all input components, which have their values 
 * bound to a bean reference. This is used both on render to give them their
 * initial values, and on submission to apply the input value to the model.
 * <br> Immediate descendents are UIInput(One) and UIInputMany.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class UIBound extends UIComponent {
  public String valuebinding;
}
