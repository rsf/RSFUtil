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
 * In case of an HTTP submission, these are encoded as key/value in the
 * request map (via hidden form fields) as follows:
 * <br>key = componentid-fossil, value=#{bean.member}oldvalue
 * <br>Alternatively, this SVE may represent a "fast EL" binding, without
 * a component. In this case, it has the form
 * <br>key = #{el.lvalue}, value = rvalue, where rvalue may represent an EL
 * rvalue, a SAXLeafType or a Object.
 * <br>The actual value submission is encoded in the RenderSystem for UIInputBase,
 * but is generally expected to simply follow
 * <br>key = componentid, value = newvalue.
 * 
 */
public class SubmittedValueEntry {
  /** The suffix appended to the component fullID in order to derive the key
   * for its corresponding fossilized binding.
   */
  public static final String FOSSIL_SUFFIX = "-fossil";
  public static final String DELETION_BINDING = "*"+FOSSIL_SUFFIX;
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

  /** A utility method to determine whether a given key (from the request map)
   * represents a Fossilised binding, i.e. it ends with the suffix {@link #FOSSIL_SUFFIX}.
   */
  public static boolean isFossilisedBinding(String key) {
    return key.endsWith(FOSSIL_SUFFIX);
  }
  /** Attempts to construct a SubmittedValueEntry on a key/value pair found in
   * the request map, for which isFossilisedBinding has already returned <code>true</code>. 
   * In order to 
   * complete this (non-deletion) entry, the <code>newvalue</code> field must
   * be set separately.
   * @param key
   * @param value
   */

  public static SubmittedValueEntry parseFossil(String key, String value) {
    SubmittedValueEntry togo = new SubmittedValueEntry();
    int endcurly = value.indexOf('}');
    togo.valuebinding = value.substring(2, endcurly);
    togo.oldvalue = value.substring(endcurly + 1);
    togo.componentid = key.substring(0, key.length() - 
        SubmittedValueEntry.FOSSIL_SUFFIX.length());
    if (togo.componentid.equals(SubmittedValueEntry.DELETION_BINDING)) {
      togo.isdeletion = true;
      togo.componentid = null;
    }
    return togo;
  }
  
  public SubmittedValueEntry () {
    
  }
  
}
