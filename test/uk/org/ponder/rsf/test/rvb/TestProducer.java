/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.rvb;

import java.util.List;

import uk.org.ponder.arrayutil.ListUtil;
import uk.org.ponder.beanutil.entity.EntityID;
import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.EntityCentredViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class TestProducer implements ViewComponentProducer, NavigationCaseReporter {

  public String getViewID() {
    return RequestLauncher.TEST_VIEW;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {

  }

  public List reportNavigationCases() {
    return ListUtil.instance(new NavigationCase(
        new EntityCentredViewParameters("null", 
            new EntityID("testentity", null))));
  }
}
