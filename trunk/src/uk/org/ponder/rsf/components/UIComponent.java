/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.util.RSFUtil;

/**
 * UIComponent is the base of the entire RSF component hierarchy. Components
 * derived from this class may either be containers derived from UIContainer,
 * or else leaf components peering with target dialect tags.
 * Note that Components form a containment hierarchy ONLY to allow nested 
 * repetitive domains. This class is mutually referential with UIContainer.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class UIComponent {
  /** This ID corresponds to the rsf:id in the view template, and is parsed
   * by use of the class SplitID.
   */
  public String ID;
  // fullid is the full path to this component
  // structure: ID1-prefix:ID1-suffix:localID1:ID2-prefix:ID2-suffix:localID2:etc.
  private String fullID;

  /** The algorithm used here must deterministically generate a string ID
   * globally unique within the containing component tree (the View), by 
   * appending path components derived from IDs and local IDs found at each
   * level of UIContainer. This algorithm should be "accessible" to simple 
   * environments such as XSLTs since they will need to operate it to generate
   * inter-component references within a view (for example to express any
   * EL dependencies).
   * <p>
   * The structure of the ID forms colon-separated "triples", one for each
   * container in the path, ending with the rsf:id of any leaf component, e.g.
   * ID1-prefix:ID1-suffix:localID1:ID2-prefix:ID2-suffix:localID2:etc.
   */
  public String getFullID() {
    if (fullID == null) {
      fullID = RSFUtil.computeFullID(this);
    }
    return fullID;
  }
  
  /** Updates the full ID of this component with the supplied value. This
   * is an "emergency" method to be used only as a last resort. Within RSF
   * it is necessary to ensure that UIBound components arising as direct children
   * of "composite" parents can have their IDs set correctly before value
   * fixup.
   */
  
  public void updateFullID(String fullID) {
    this.fullID = fullID;
  }
  
  /** The containing parent of this component, or <code>null</code> for the
   * UIContainer representing the view root.
   */
  public UIContainer parent;
  
}