/*
 * Created on 12-Jan-2006
 */
package uk.org.ponder.rsf.components;

/** A "placeholder" component (similar to UIReplicator) implementing a boolean
 * test. Based on whether two object values (the lvalue and the rvalue) compare
 * equal by means of Java Object.equals(), one of two genuine components specified
 * in a serialized "proto-tree" will be placed in the view tree prior to rendering.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 */
public class UISwitch extends UIComponent {
  /** An EL reference from which the object to form the left side of the comparison
   * can be fetched from the request container.
   */
  public String lvalue;
  /** An Object (specified in-line in the serialized form) which will form the 
   * right-hand side of the comparison. If this is <code>null</code>, the
   * object will instead be fetched via <code>elrvalue</code>.
   */
  public Object rvalue;
  /** An EL reference from which the object to form the left side of the comparison
   * can be fetched from the request container. Ignored if <code>lvalue</code> is not null.
   */ 
  public String elrvalue;
  
  /** The component to be rendered if the lvalue and rvalue compare equal */
  public UIComponent truecomponent;
  /** The component to be rendered if the lvalue and rvalue do not compare equal */
  public UIComponent falsecomponent;
}
