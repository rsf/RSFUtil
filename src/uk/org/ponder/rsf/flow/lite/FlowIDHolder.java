/*
 * Created on Nov 25, 2005
 */
package uk.org.ponder.rsf.flow.lite;

/** Used to hold the ID of the Flow that is governing the current request 
 * cycle. We cannot use a simple String since this would break the reference
 * chain when it was assigned to.
 * <p>The value of the held String is set when the relevant FlowActionProxyBean
 * is sent the "start" message, and it will be persisted across the flow
 * by the normal means of the StatePreservationManager, using whatever 
 * Strategy is specified for this bean.
 * <p>The value of the string is READ by the ARIResolver which will return
 * a suitably initialised FlowLite ARI when requested.
 * //TODO: Think more carefully about any other beans which may be time-dependent
 * WRT. state restoration. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class FlowIDHolder {
  // two flow-scoped members recording the flow coordinates
  public String flowID;
  public String flowStateID;
  public String flowtoken;
  // a request-scoped member allowing fine tracking of the flow state per request.
  public String requestFlowStateID;
  public String toString() {
    return "Flow ID " + flowID + " flowStateID " + flowStateID + " flowtoken "
     + flowtoken + " requestFlowStateID " + requestFlowStateID;
  }
  public boolean isEmpty() {
    return flowID == null && flowStateID == null && flowtoken == null 
    && requestFlowStateID == null;
  }
}
