/*
 * Created on Aug 8, 2005
 */
package uk.org.ponder.rsf.components;


/**
 * UIBranchContainer represents a "branch point" in the IKAT rendering process,
 * rather than simply just a level of component containment.
 * <p>
 * UIBranchContainer has responsibility for managing naming of child components,
 * as well as separate and parallel responsibility for forms. The key to the
 * child map is the ID prefix - if the ID has no suffix, the value is the single
 * component with that ID at this level. If the ID has a suffix, indicating a
 * repetitive domain, the value is an ordered list of components provided by the
 * producer which will drive the rendering at this recursion level.
 * <p>
 * It is assumed that an ID prefix is globally unique within the tree, not just
 * within its own recursion level - i.e. IKAT resolution takes place over ALL
 * components sharing a prefix throughout the template. This is "safe" since
 * "execution" will always return to the call site once the base (XML) nesting
 * level at the target is reached again.
 * <p>
 * "Leaf" rendering classes <it>may</it> be derived from UISimpleContainer -
 * only concrete instances of UIBranchContainer will be considered as
 * representatives of pure branch points. By the time fixups have concluded, all
 * non-branching containers (e.g. UIForms) MUST have been removed from non-leaf
 * positions in the component hierarchy.
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
   * within its container. Where BranchContainers are created in a loop, supply
   * a localID by using the {@link #make(UIContainer, String, String)}
   * constructor.
   * 
   * @see #make(UIContainer, String, String)
   */
  public static UIBranchContainer make(UIContainer parent, String ID) {
    return make(parent, ID, "");
  }
  
}
