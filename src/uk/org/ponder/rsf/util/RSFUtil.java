/*
 * Created on Oct 13, 2004
 */
package uk.org.ponder.rsf.util;

import java.util.HashMap;

import uk.org.ponder.errorutil.SubmittedValueEntry;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInput;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class RSFUtil {
  public static String stripEL(String el) {
    if (el.startsWith("#{") && el.endsWith("}")) {
      return el.substring(2, el.length() - 1);
    }
    else
      return null;
  }

  /**
   * Creates a fossilised value binding that will be active for ALL methods of
   * submitting the current form. A UIMessage will be created as direct child of
   * the supplied parent to collect messages targetted at the specified input
   * component. The currentvalue supplied will be overridden by any previously
   * cached ErrorStateEntry information stored for this thread. ENSURE that the
   * target parent lies WITHIN the same naming parent as the specified input
   * component!
   */
  public static void setFossilisedBinding(UIContainer parent, UIInput toset,
      String binding, String currentvalue) {
    RSFFactory.addParameter(parent, toset.getFullID()
        + SubmittedValueEntry.FOSSIL_SUFFIX, binding + currentvalue);

  }

  /**
   * Creates a fossilised value binding that will only function for the
   * specified command link. The value will NOT be applied if the form is
   * submitted by some means other than the specified link. A Message component
   * must be added manually. This value is decoded in SubmittedValueEntry constructor.
   */
  public static void setFossilisedBinding(UIInput toset, UICommand trigger,
      String binding, String currentvalue) {

    String compid = toset.getFullID();
    addCommandLinkParameter(trigger,
        compid + SubmittedValueEntry.FOSSIL_SUFFIX, binding + currentvalue);
  }

  public static void addDeletionBinding(UICommand trigger,
      String deletebinding, String todelete) {
    addCommandLinkParameter(trigger, SubmittedValueEntry.DELETION_BINDING,
        deletebinding + todelete);
  }

  public static void addCommandLinkParameter(UICommand trigger, String key,
      String value) {
    if (trigger.commandparams == null) {
      trigger.commandparams = new HashMap();
    }
    trigger.commandparams.put(key, value);
  }

}