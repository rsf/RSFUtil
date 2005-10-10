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
 * <br>The actual value submission is encoded in the RenderSystem for UIInputBase,
 * but is generally expected to simply follow
 * <br>key = componentid, value = newvalue.
 */
public class SubmittedValueEntry {
  public static final String FOSSIL_SUFFIX = "-fossil";
  public static final String DELETION_BINDING = "*"+FOSSIL_SUFFIX;
  public static final String COMMAND_LINK_PARAMETERS = "command link parameters";
//  RSFFactory.addParameter(parent, toset.getFullID()
//      + SubmittedValueEntry.FOSSIL_SUFFIX, binding + currentvalue);
  // key = {value-binding}componentid-fossil, value = oldvalue OLD
  // key = componentid-fossil, value=#{bean.member}oldvalue NEW
  // deletion - id is *, does not enter rsvc.
  public String valuebinding;
  public String componentid;
  // this is either a String or a String[]
  public Object oldvalue;
  public boolean isdeletion = false;

  public static boolean isFossilisedBinding(String key) {
    return key.endsWith(FOSSIL_SUFFIX);
  }
  
  public SubmittedValueEntry(String key, String value) {
    int endcurly = value.indexOf('}');
    valuebinding = value.substring(2, endcurly);
    oldvalue = value.substring(endcurly + 1);
    componentid = key.substring(0, key.length() - 
        FOSSIL_SUFFIX.length());
    if (componentid.equals(DELETION_BINDING)) {
      isdeletion = true;
      componentid = null;
    }
  }
}
