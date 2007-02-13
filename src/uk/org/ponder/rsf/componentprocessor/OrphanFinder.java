/*
 * Created on Nov 27, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.util.Logger;

/** Debugging processor that notes non-submitting controls that expect to be
 * submitting.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class OrphanFinder implements ComponentProcessor {
  private Set expectsubmit = new HashSet();
  private Set submitting = new HashSet();
  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIBound) {
      UIBound toprocess = (UIBound) toprocesso;
      if (toprocess.valuebinding != null && toprocess.fossilize 
          && toprocess.willinput) {
        expectsubmit.add(toprocess.getFullID());
      }
    }
    else if (toprocesso instanceof UICommand) {
      expectsubmit.add(toprocesso.getFullID());
    }
    else if (toprocesso instanceof UIForm) {
      UIForm toprocess = (UIForm) toprocesso;
      submitting.addAll(toprocess.submittingcontrols);
    }
  }
  public void report() {
    for (Iterator cit = expectsubmit.iterator(); cit.hasNext();) {
      Object key = cit.next();
      if (!submitting.contains(key)) {
        Logger.log.warn("Control with full ID " + key + " expects submission " +
                "but has not been registered with any form");
      }
    }
  }
}
