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
public class UIDeletionBinding extends UIParameter {
  public UIDeletionBinding() {}
  public UIDeletionBinding(String deletebinding, Object deletetarget) {
    this.deletebinding = ELReference.make(deletebinding);
    this.deletetarget = deletetarget;
  }
  public UIDeletionBinding(String deletebinding) {
    this(deletebinding, null);
  }
  /** An EL path (including #{}) 
   * specifying the location in the bean model the deletion
   * is to occur. If the target field is left blank, this binding specifies
   * the removal of the object specified by the tail portion of the path
   * (after the final ".").
   */
  public ELReference deletebinding;
  public Object deletetarget;
}
