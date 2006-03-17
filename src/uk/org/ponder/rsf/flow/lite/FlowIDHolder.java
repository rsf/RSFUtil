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
  private String flowID;
  private String flowStateID;
  private String flowtoken;
  // a request-scoped member allowing fine tracking of the flow state per request.
  private String requestFlowStateID;
  public String toString() {
    return "Flow ID " + getFlowID() + " flowStateID " + getFlowStateID() + " flowtoken "
     + getFlowToken() + " requestFlowStateID " + getRequestFlowStateID();
  }
  public boolean isEmpty() {
    return getFlowID() == null && getFlowStateID() == null && getFlowToken() == null 
    && getRequestFlowStateID() == null;
  }
  public void setFlowID(String flowID) {
    this.flowID = flowID;
  }
  public String getFlowID() {
    return flowID;
  }
  public void setFlowStateID(String flowStateID) {
    this.flowStateID = flowStateID;
  }
  public String getFlowStateID() {
    return flowStateID;
  }
  public void setFlowToken(String flowtoken) {
    this.flowtoken = flowtoken;
  }
  public String getFlowToken() {
    return flowtoken;
  }
  public void setRequestFlowStateID(String requestFlowStateID) {
    this.requestFlowStateID = requestFlowStateID;
  }
  public String getRequestFlowStateID() {
    return requestFlowStateID;
  }
}
