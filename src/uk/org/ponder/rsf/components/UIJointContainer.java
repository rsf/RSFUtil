/*
 * Created on 16-Sep-2006
 */
package uk.org.ponder.rsf.components;

/** A "two-faced" container that expresses a discontinuous "joint" between
 * two segments of template. The container appears in its containing context
 * as a BranchContainer with the details contained in the parent - however
 * the IKAT branch resolution behaviour is "as if" it in fact had the ID
 * given by its <code>jointID</code> field. This is the core container allowing
 * modularity of RSF templates and producers ("components").
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class UIJointContainer extends UIBranchContainer {
  public String jointID;
  
  public UIJointContainer (UIContainer parent, String ID, String jointID) {
    this.ID = ID;
    this.jointID = jointID;
    parent.addComponent(this);
  }
  
  public UIJointContainer (UIContainer parent, String ID) {
    this(parent, ID, null);
  }
  
}
