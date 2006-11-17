/*
 * Created on Oct 10, 2005
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** An object summarising the return from the ActionResultInterpreter. This
 * embodies the flow of the application by determining the resulting view to
 * be shown on the next cycle, as well as giving instructions for the disposal
 * or propagation of the current request-scope state.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class ARIResult {
  /** The request scope state will be propagated to the token held by the
   * view parameters shown in the next request cycle.
   */
  public static final String PROPAGATE = "propagate";
  /** No propagation will be performed of request state - in addition any
   * tokens sharing this state will be unlinked from it. Return this code
   * at the successful conclusion of either a multi-request wizard, or of a 
   * single request. 
   */
  public static final String FLOW_END = "flow-end";
 /** THIS VALUE IS NOT YET SUPPORTED! Indicate that the following view
  * will represent a "forked" instance of the flow in progress, with a clone
  * of all of its flow state. Will anyone really want this?
  */
  public static final String FLOW_FORK = "flow-fork";
  
  /** A flow that will begin and end in a single cycle - i.e. will 
   * immediately enter the "end-flow" condition. The current view must be
   * free of any flow state, end-flow or otherwise. In effect, perform 
   * FLOW_FASTSTART and FLOW_END simultaneously.
   */
  public static final String FLOW_ONESTEP = "flow-onestep";

  /** An action name representing the start of a flow. This kind of flow is
   * safe to launch from the end-state of an existing flow. Although not
   * from an internal state of an existing flow. **/
  public static final String FLOW_START = "flow-start";
  
  /** Launches a new flow, in "fast" mode, i.e. performing the same 
   * preservation that is performed in interior states. This may not
   * be used in either internal or end states of another flow.
   */
  public static final String FLOW_FASTSTART = "flow-faststart";
  
    /** Propagate the request scope state only as an "error" state - i.e. this
     * represents an erroneous submission from a single-request submission.
     * The default implementation will expire the storage of this state on
     * a more rapid schedule than "normal" multi-request state (on the order
     * of minutes rather than hours).
     */
 
 // public static final String PROPAGATE_ERROR_ONLY = "propagate error only";
  /** The view to be shown on the next cycle. The <code>tokenid</code> field
   * will be overwritten by RSF with a unique token before it is dispensed
   * to the client via the client redirect.
   */
  public AnyViewParameters resultingview;
  
  /** Use this access method if loading via a proxy **/
  
  public AnyViewParameters getResultingView() {
    return resultingview;
  }
  /** A result code indicating the propagation status of any multi-request state
   * required by the next view to be rendered. This code is chosen from one of the
   * FLOW_ String values above.
   */
  public String propagatebeans;
  /** Access method for the above field.
   * @return
   */
  // TODO: convert this to a Pea Proxy
  public String getPropagateBeans() {
    return propagatebeans;
  }
}
