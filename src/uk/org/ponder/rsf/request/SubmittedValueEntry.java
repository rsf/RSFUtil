/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.rsf.request;

import java.io.Serializable;

/**
 * Represents a submitted value as received from a user submission. For 
 * full coverage, we must know 
 * <br>- the (full) ID of the component producing the submission
 * <br>- the value binding for the submitted value
 * <br>- the initial value presented to the UI when the view was rendered.
 * These values, once accumulated, are stored inside the
 * {@link RequestSubmittedValueCache} 
 * In addition to encoding a component submission, an SVE may either 
 * represent a deletion binding or a pure EL binding.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class SubmittedValueEntry implements Serializable {
 
  /** The key used in the parameters of a command link (and hence in the 
   * servlet request) whose value is the method binding EL to be invoked on
   * this request.
   */
  public static final String FAST_TRACK_ACTION = "Fast track action";
  
  /** The key used to find the ID of the <it>submitting control</it> used
   * to produce this submission, which is that of the corresponding UIForm 
   * component. For "WAP-style" forms this may well be the same as the
   * physical submitting control.
   */
  public static final String SUBMITTING_CONTROL = "Submitting control";
  /** The EL path (without #{}) that is the l-value target of this binding.
   */
  public String valuebinding;
  /** The EL path (without #{}) that may be used to look up a reshaper
   * to be applied before converting this SVE into a DAR.
   */
  public String reshaperbinding;
  /** The full ID of the component giving rise to this submission. 
   * This will be null for a non-component binding. */
  
  public String componentid;
  /** The value held by the component at the time the view holding it was
   * rendered. This was the initial value seen by the user at render time.
   * Either an object of type String or String[]. Holds an additional value
   * (binding or EL) in the case of a non-component binding.
   */
  public Object oldvalue;
  /** The value received back to the system via the (current) submission.
  * Initially set to the raw String[] value in the request, but normalised
  * to (at least) a UIType by FossilizedConverted.fixupNewValue. 
  */
  public Object newvalue;
  /** Holds <code>true</code> in the case the rvalue (encoded in oldvalue)
   * of this non-component binding represents an EL binding, rather
   * than a serialised object */
  public boolean isEL;
  /** Holds <code>true</code> if this submitted value is a deletion binding.
   * <code>isEL</code> may also be <code>true</code>, and may have been the
   * cause of filling in <code>newvalue</code>
   */
  public boolean isdeletion = false;
  /** If set to <code>true</code> the application of this SVE will pass all
   * "unchanged value culling" and be definitely applied to the model.
   */
  public boolean mustapply = false;
}
