/*
 * Created on Nov 12, 2005
 */
package uk.org.ponder.rsf.state;

/** Called for every RSVC entry just before its value is applied to the
 * model, to give a chance for an application-defined version check. 
 * In general if the version check fails, the method will throw some
 * form of exception, and in addition ought to add a TargettedMessage
 * to the current error state highlighting the erroneous value.
 * <b>For extreme customisability, this message could even be specifically
 * detected by the ARI and MRS flow directed to another path.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface VersionCheckPolicy {
  public void checkOldVersion(SubmittedValueEntry sve);
}
