/*
 * Created on 16-Sep-2006
 */
package uk.org.ponder.rsf.components;

/**
 * A "two-faced" container that expresses a discontinuous "joint" between two
 * segments of template. The container appears in its containing context as a
 * BranchContainer with the details contained in the parent - however the IKAT
 * branch resolution behaviour is "as if" it in fact had the ID given by its
 * <code>jointID</code> field. This is the core container allowing modularity
 * of RSF templates and producers ("components").
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 */

public class UIJointContainer extends UIBranchContainer {
  public String jointID;

  /**
   * Create a new UIJointContainer and add it to the component tree.
   * 
   * @param parent The container to receive this newly created joint container.
   * @param ID The "client ID" or "source ID" of this container, that is, the
   *          rsf:id under which the host template and producer refer to the
   *          component.
   * @param jointID The "joint ID" or "target ID" of this container, that is,
   *          the rsf:id under which the implementing or component template
   *          refer to the component.
   */
  public UIJointContainer(UIContainer parent, String ID, String jointID) {
    this.ID = ID;
    this.jointID = jointID;
    parent.addComponent(this);
  }
  
  /**
   * @see UIJointContainer#UIJointContainer(UIContainer, String, String)
   * @param localID The localID, used to distinguish repeated replicates of this
   * joint which occur in the same scope (in just the same way as plain branches)
   */
  
  public UIJointContainer(UIContainer parent, String ID, String jointID, String localID) {
    this.ID = ID;
    this.jointID = jointID;
    this.localID = localID;
    parent.addComponent(this);
  }

  /** @see UIJointContainer#UIJointContainer(UIContainer, String, String)
   */
  
  public UIJointContainer(UIContainer parent, String ID) {
    this(parent, ID, null);
  }

}
