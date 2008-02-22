/*
 * Created on Nov 16, 2005
 */
package uk.org.ponder.rsf.components;

/** A deletion binding specifies the removal of an element of the
 * bean model from its container. The fields in the UIParameter parent will
 * be filled in by a component fixup processor.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class UIDeletionBinding extends UIBinding {
  public UIDeletionBinding() {}
  
  public UIDeletionBinding(String deletebinding, Object deletetarget) {
    this.deletebinding = ELReference.make(deletebinding);
    this.deletetarget = deletetarget;
  }
  
  public UIDeletionBinding(String deletebinding, Object deletetarget, String encoding) {
    this.deletebinding = ELReference.make(deletebinding);
    this.deletetarget = deletetarget;
    this.encoding = encoding;
  }
  
  public UIDeletionBinding(String deletebinding) {
    this(deletebinding, null);
  }
  /** An EL path
   * specifying the location in the bean model the deletion
   * is to occur. If the {@link #deletetarget} field is left blank, this binding specifies
   * the removal of the object specified by the tail portion of the path
   * (after the final ".").
   */
  public ELReference deletebinding;
  /** A specification of the object to be deleted, either an ELReference 
   * referring to the future path from which the object may be fetched, or
   * a literal specification of the object itself. May be left <code>null</code>
   * in the case the deletion key is specified implicitly in the binding EL itself.
   */
  public Object deletetarget;
}
