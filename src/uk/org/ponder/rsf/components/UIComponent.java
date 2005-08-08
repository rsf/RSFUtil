/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Components form a hierarchy ONLY to allow nested repetitive domains. 
 * The key to the child map is the ID prefix - if the ID has no suffix, the
 * value is the single component with that ID at this level. If the ID
 * has a suffix, indicating a repetitive domain, the value is an ordered
 * list of components provided by the producer which will drive the 
 * rendering at this recursion level.
 * It is assumed that an ID prefix is globally unique within the tree, not
 * just within its own recursion level.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class UIComponent {
  public static final String NON_PEER_ID = "  non-peer  ";
  // splitid is used to locate a rendering peer.
  public SplitID ID;
  // ID assigned to assure identity on this level, within repetitive domain.
  public String localID = "";
  // fullid is the full path to this component
  // structure: ID1-prefix:ID1-suffix:localID1:ID2-prefix:ID2-suffix:localID2:etc.
  private String fullID;

  // the actual algorithm for this is irrelevant insofar as it deterministically 
  // generates a globally unique String representing this component, based
  // on its hierarchical position within domains and the supplied local ID.
  public String getFullID() {
    if (fullID == null) {
      StringBuffer togo = new StringBuffer();
      UIComponent move = this;
      while (move.parent != null) {
        togo.insert(0, ID.toString() + SplitID.SEPARATOR + localID);
        move = move.parent;
      }
      fullID = togo.toString();
    }
    return fullID;
  }
  
  public UIContainer parent;
  
}