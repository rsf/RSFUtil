/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.components;

/** A "binding" which is stored in the component tree, and will be rendered
 * as a submission that, when received, will perform an "assignment" in the
 * request scope container. This assignment will be of Java object references,
 * to the EL address specified by the <code>valuebinding</code> field.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIELBinding extends UIParameter {
  public UIELBinding() {}
  public UIELBinding(String lvalue, Object rvalue, boolean virtual) {
    valuebinding = new ELReference(lvalue);
    this.rvalue = rvalue; 
    this.virtual = virtual;
  }
  public UIELBinding(String lvalue, Object rvalue) {
    this(lvalue, rvalue, false);
  }
  /** If set to <code>true</code>, represents a "virtual" EL binding, that is,
   * one that is rendered inactive by default, to be "discovered" by some
   * client-side mechanism. Virtual bindings represent an "alternate execution path"
   * through the request container.
   */
  public boolean virtual;
  /** The "target" of this binding - an EL bean path that will be written
   * when this binding is received. Includes #{}.
   */
  public ELReference valuebinding;
  /** The r-value of this binding is an Object which will be serialised and
   * reconstructed when the binding is to be applied. If this is an ELReference
   * itself, the corresponding value will be fetched.
   */
  public Object rvalue;
}
