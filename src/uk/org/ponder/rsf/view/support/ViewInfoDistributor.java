/*
 * Created on 7 Mar 2008
 */
package uk.org.ponder.rsf.view.support;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.rsf.content.ContentTypeReceiver;
import uk.org.ponder.rsf.content.ContentTypeReporter;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReceiver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewIDReporter;
import uk.org.ponder.rsf.viewstate.ViewParameters;
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
  
  private boolean match(Object view, Class target, Class[] exceptions) {
    return (target.isInstance(view) && (exceptions == null ||
        !ArrayUtil.contains(exceptions, target)));
  }
  
  public String distributeInfo(Object view) {
    return distributeInfo(view, null);
  }
  
  public String distributeInfo(Object view, Class[] exceptions) {
    String key = ALL_VIEW_PRODUCER;
    if (match(view, ViewIDReporter.class, exceptions)) {
      key = ((ViewIDReporter) view).getViewID();
    }
    if (match(view, ViewParamsReporter.class, exceptions)) {
      ViewParamsReporter vpreporter = (ViewParamsReporter) view;
      ViewParameters viewparams = vpreporter.getViewParameters();
      if (viewparams == null) {
        throw new IllegalArgumentException("Error in view " + view + " with id " + key 
            + ": getViewParameters has returned null - this must return a concrete ViewParameters exemplar");
      }
      vpreceiver.setViewParamsExemplar(key, viewparams);
    }
    if (match(view, DefaultView.class, exceptions)) {
      vpreceiver.setDefaultView(key);
    }
    if (match(view, NavigationCaseReporter.class, exceptions)) {
      ncreceiver.receiveNavigationCases(key,
          ((NavigationCaseReporter) view).reportNavigationCases());
    }
    if (match(view, ContentTypeReporter.class, exceptions)) {
      ctreceiver.setContentType(key, 
          ((ContentTypeReporter)view).getContentType());
    }
    return key;
  }
  
}
