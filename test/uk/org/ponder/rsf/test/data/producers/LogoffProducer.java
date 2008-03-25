/*
 * Created on 24 Jul 2006
 */
package uk.org.ponder.rsf.test.data.producers;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIDeletionBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIOutput;

/**
 * A self-contained "sub-view producer", which does the work of rendering a
 * "logout" panel. This producer is shared between "LogonProducer" and
 * "WalesProducer" - this is how we create "components" in RSF, and why the need
 * to create new "first-class" framework components (like UIOutput, UIForm,
 * etc.) is extremely rare.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class LogoffProducer {
  public void fillComponents(UIContainer tofill, String username) {
    UIBranchContainer logged = UIBranchContainer.make(tofill,
        "login-loggedin-pane:");
    UIOutput.make(logged, "login-user", username);
    UIForm form = UIForm.make(logged, "basic-form");

    UICommand logoff = UICommand.make(form, "login-logoff");
    // rather than just clear the fields from "LogonBean", completely remove
    // its storage from the session.
    logoff.parameters.add(new UIDeletionBinding("#{destroyScope.logonScope}"));
  }
}
