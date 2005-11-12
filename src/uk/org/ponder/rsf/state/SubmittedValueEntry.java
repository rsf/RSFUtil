/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.state;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * Represents a submitted value as received from a user submission. For 
 * full coverage, we must know 
 * <br>- the (full) ID of the component producing the submission
 * <br>- the value binding for the submitted value
 * <br>- the initial value presented to the UI when the view was rendered.
 * These values, once accumulated, are stored inside the 
 * {@see uk.org.ponder.rsf.state.RequestSubmittedValueCache}
 */
public class SubmittedValueEntry {
 
  public static final String COMMAND_LINK_PARAMETERS = "command link parameters";
  /** The key used in the parameteres of a command link (and hence in the 
   * servlet request) whose value is the method binding EL to be invoked on
   * this request.
   */
  public static final String FAST_TRACK_ACTION = "Fast track action";
//  RSFFactory.addParameter(parent, toset.getFullID()
//      + SubmittedValueEntry.FOSSIL_SUFFIX, binding + currentvalue);
  // key = {value-binding}componentid-fossil, value = oldvalue OLD
  // key = componentid-fossil, value=#{bean.member}oldvalue NEW
  // deletion - id is *, does not enter rsvc.
  public String valuebinding;
  /** The full ID of the component giving rise to this submission. 
   * This may be null if this represents a fast EL submission */
  public String componentid;
  /** The value held by the component at the time the view holding it was
   * rendered. This was the initial value seen by the user at render time.
   * Either an object of type String or String[].
   */
  public Object oldvalue;
  /** The value received back to the system via the (current) submission.
  * Either a String or a String[]. However, if it is a String, there are some
  * interesting possibilities - i) String or SaxLeafType, ii) EL reference,
  * iii) XML-encoded object tree. Isn't this fun! (really!)
  */
  public Object newvalue;
  /** Holds <code>true</code> if this submitted value is a deletion binding,
   * in which case both oldvalue and newvalue will be <code>null</code>
   */
  public boolean isdeletion = false;
  
}
