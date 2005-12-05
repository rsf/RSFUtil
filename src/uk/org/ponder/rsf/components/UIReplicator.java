/*
 * Created on Nov 25, 2005
 */
package uk.org.ponder.rsf.components;

public class UIReplicator extends UIComponent {
  /** The character that, when it appears in an EL reference in an expanding
   * container, will be replaced by the replicated localID.
   */
  public static final String LOCALID_WILDCARD = "*";
  /** An EL reference to a collection, list, array (multi-value in general)
   * somewhere in request scope to use to replicate the provided container.
   */
  public String valuebinding;
  /** An object encapsulating the strategy to be used for mapping from objects
   * found in the targetted list to the EL path that they will be referenced
   * at. 
   */
  public Object idstrategy;
  /** The container to be replicated by iteration over the container 
   * discovered at the above binding.
   */
  public UIBranchContainer component;
}
