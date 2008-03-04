/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.navigation;

import java.util.List;

import uk.org.ponder.arrayutil.ListUtil;
import uk.org.ponder.rsf.bare.RequestLauncher;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class TestProducer implements ViewComponentProducer, NavigationCaseReporter, 
 ViewParamsReporter {

  public String getViewID() {
    return RequestLauncher.TEST_VIEW;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    ELParams elparams = (ELParams) viewparams;
    UIForm form = UIForm.make(tofill, "form");
    UICommand.make(form, "command", elparams.el);
  }

  public List reportNavigationCases() {
    return ListUtil.instance(new NavigationCase[] {
        new NavigationCase("first", new SimpleViewParameters("first")),
        new NavigationCase(new SimpleViewParameters("default")),
        new NavigationCase("first", new SimpleViewParameters("second")),
        new NavigationCase("second", new SimpleViewParameters("third")),
        new NavigationCase(new SimpleViewParameters("default2"))
        });
  }

  public ViewParameters getViewParameters() {
    return new ELParams(null);
  }

}
