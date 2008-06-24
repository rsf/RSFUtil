/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.viewerror;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageException;
import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class TestProducer implements ViewComponentProducer, DefaultView {

  public String getViewID() {
    return RequestLauncher.TEST_VIEW;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    throw new TargettedMessageException(new TargettedMessage("failure"));
  }

}
