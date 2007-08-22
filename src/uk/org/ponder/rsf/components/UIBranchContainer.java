/*
 * Created on Aug 8, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * UIBranchContainer represents a "branch point" in the IKAT rendering process.
 * At a branch point, the renderer may either branch to another part of the 
 * current template, or a different template, to a point with the same RSF ID. This
 * kind of branch may also form a loop, where there are sibling branches with the
 * same ID. A "forced branch" can be made to a tag with a different ID by using
 * a {@link UIJointContainer}.
 * <p>
 * UIBranchContainer has responsibility for managing naming of child components. 
 * The key to the child map is the ID prefix - if the ID has no suffix, 
 * the value is the single component with that ID at this level. 
 * If the ID has a suffix, indicating a repetitive domain, the value is an 
 * ordered list of components provided by the producer which will drive 
 * the rendering at this recursion level.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class UIBranchContainer extends UIContainer {
  /**
   * Constructs a "repeating" BranchContainer, uniquely identified by the
   * "localID" passed as the 3rd argument. Suitable, for example, for creating a
   * table row.
   * 
   * @param parent The parent container to which the returned branch should be
   *          added.
   * @param ID The RSF ID for the branch (must contain a colon character)
   * @param localID The local ID identifying this branch instance (must be
   *          unique for each branch with the same ID in this branch)
   */
  public static UIBranchContainer make(UIContainer parent, String ID,
      String localID) {
    if (ID.indexOf(':') == -1) {
      throw new IllegalArgumentException(
          "Branch container ID must contain a colon character :");
    }
    UIBranchContainer togo = new UIBranchContainer();
    togo.ID = ID;
    togo.localID = localID;
    parent.addComponent(togo);
    return togo;
  }

  /**
   * Constructs a simple BranchContainer, used to group components or to cause a
   * rendering switch. Suitable where there will be just one branch with this ID
   * within its container. Where BranchContainers are created in a loop, they will
   * have the <code>localID</code> automatically assigned in an integer sequence.
   * 
   * @see #make(UIContainer, String, String)
   */
  public static UIBranchContainer make(UIContainer parent, String ID) {
    return make(parent, ID, "");
  }
  
}
