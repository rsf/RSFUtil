/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.mapping.DataAlterationRequest;

/** A "binding" which is stored in the component tree, and will be rendered
 * as a submission that, when received, will perform an "assignment" in the
 * request scope container. This assignment will be of Java object references,
 * to the EL address specified by the <code>valuebinding</code> field.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIELBinding extends UIBinding {
  
  public UIELBinding() {}
  
  /** Create a binding that will assign the Object or EL reference <code>rvalue</code>
   * to the EL path <code>lvalue</code> in the following request cycle.
   * @param lvalue the EL reference at which the specified object will be assigned.
   * @param rvalue Either a literal Object, or an {@link ELReference} from which it
   * can be read in the following request, which is to be assigned to the lvalue path.
   * @param virtual <code>true</code> if this is a "virtual binding" - that is one that
   * will not be automatically applied by the client action, but is for the instruction
   * of client-side code
   * @param encoding An encoding form in which the rvalue is to be encoded, either
   *  {@link DataAlterationRequest#JSON_ENCODING} (the default) 
   *  or {@link DataAlterationRequest#XML_ENCODING}.
   */
  public UIELBinding(String lvalue, Object rvalue, boolean virtual, String encoding) {
    valuebinding = new ELReference(lvalue);
    this.rvalue = rvalue; 
    this.virtual = virtual;
    this.encoding = encoding;
  }
  
  public UIELBinding(String lvalue, Object rvalue, boolean virtual) {
    this(lvalue, rvalue, virtual, DataAlterationRequest.JSON_ENCODING);
  }
  
  public UIELBinding(String lvalue, Object rvalue, String encoding) {
    this(lvalue, rvalue, false, DataAlterationRequest.JSON_ENCODING);
  }
  
  /** Create a binding that will assign the Object or EL reference <code>rvalue</code>
   * to the EL path <code>lvalue</code> in the following request cycle. 
   */
  public UIELBinding(String lvalue, Object rvalue) {
    this(lvalue, rvalue, false);
  }
  /** The "target" (lvalue) of this binding - an EL bean path that will be written
   * when this binding is received. Includes #{}.
   */
  public ELReference valuebinding;
  /** The r-value of this binding is an Object which will be serialised and
   * reconstructed when the binding is to be applied. If this is an ELReference
   * itself, the corresponding value will be fetched.
   */
  public Object rvalue;

}
