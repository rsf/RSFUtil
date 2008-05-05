/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.render;

import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class TestProducer implements ViewComponentProducer {

  public String getViewID() {
    return RequestLauncher.TEST_VIEW;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    UIVerbatim.make(tofill, "verbatim-test", "verbatim.label").setMessageKey();
    
    UIMessage.make(tofill, "message-holder", "message.key", new Object[] {
        new ELReference("testString")});
    }
}
