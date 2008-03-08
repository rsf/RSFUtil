/*
 * Created on 7 Mar 2008
 */
package uk.org.ponder.rsf.view.support;

import uk.org.ponder.rsf.content.ContentTypeReceiver;
import uk.org.ponder.rsf.content.ContentTypeReporter;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReceiver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewIDReporter;
import uk.org.ponder.rsf.viewstate.ViewParamsReceiver;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/** An infrastructural class accepting info from view beans and distributing it
 * to interested parties.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class ViewInfoDistributor {

  private ViewParamsReceiver vpreceiver;
  private NavigationCaseReceiver ncreceiver;
  private ContentTypeReceiver ctreceiver;
  // This key is used to identify a producer that *might* produce components
  // in all views. Upgrade this architecture once we think a little more about
  // how we might actually want to locate view templates and component
  // producers relative to views (view IDs or in general, ViewParams)
  public static final String ALL_VIEW_PRODUCER = "  all views  ";

  public void setViewParametersReceiver(ViewParamsReceiver vpreceiver) {
    this.vpreceiver = vpreceiver;
  }

  public void setNavigationCaseReceiver(NavigationCaseReceiver ncreceiver) {
    this.ncreceiver = ncreceiver;
  }

  public void setContentTypeReceiver(ContentTypeReceiver ctreceiver) {
    this.ctreceiver = ctreceiver;
  }
  
  public String distributeInfo(Object view) {
    String key = ALL_VIEW_PRODUCER;
    if (view instanceof ViewIDReporter) {
      key = ((ViewIDReporter) view).getViewID();
    }
    if (view instanceof ViewParamsReporter) {
      ViewParamsReporter vpreporter = (ViewParamsReporter) view;
      vpreceiver.setViewParamsExemplar(key, vpreporter.getViewParameters());
    }
    if (view instanceof DefaultView) {
      vpreceiver.setDefaultView(key);
    }
    if (view instanceof NavigationCaseReporter) {
      ncreceiver.receiveNavigationCases(key,
          ((NavigationCaseReporter) view).reportNavigationCases());
    }
    if (view instanceof ContentTypeReporter) {
      ctreceiver.setContentType(key, 
          ((ContentTypeReporter)view).getContentType());
    }
    return key;
  }
  
}
