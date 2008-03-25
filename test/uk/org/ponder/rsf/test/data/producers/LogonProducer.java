package uk.org.ponder.rsf.test.data.producers;

import java.util.List;

import uk.org.ponder.arrayutil.ListUtil;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.test.data.beans.LogonBean;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * A ViewProducer which demonstrates RSF scoped beans. These beans are stored in
 * an RSF TokenStateHolder, which "out of the box" uses HTTP (or other) session
 * storage.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class LogonProducer implements ViewComponentProducer, DefaultView, NavigationCaseReporter {
  public static final String VIEW_ID = "logon";
  private LogonBean logonbean;
  private LogoffProducer logoffproducer;

  public void setLogoffProducer(LogoffProducer logoffproducer) {
    this.logoffproducer = logoffproducer;
  }

  public void setLogonBean(LogonBean logonbean) {
    this.logonbean = logonbean;
  }

  public String getViewID() {
    return VIEW_ID;
  }

  public void fillComponents(UIContainer tofill, ViewParameters viewparamso,
      ComponentChecker arg2) {
    if (logonbean.name == null) {
      UIBranchContainer.make(tofill, "content-not-logged-in-pane:");
      UIBranchContainer notlogged = UIBranchContainer.make(tofill,
          "login-not-logged-in-pane:");
      UIForm form = UIForm.make(notlogged, "basic-form");
      UIInput.make(form, "login-user", "#{logon.name}");
      UIInput.make(form, "login-password", "#{logon.password}");

      UICommand.make(form, "login-login");  
    }
    else {
      UIBranchContainer pane = UIBranchContainer.make(tofill,
          "content-logged-in-pane:");
      logoffproducer.fillComponents(tofill, logonbean.name);
    }
  }

  public List reportNavigationCases() {
    return ListUtil.instance(new NavigationCase(new SimpleViewParameters(UserDataView.VIEW_ID)));
  }

}
