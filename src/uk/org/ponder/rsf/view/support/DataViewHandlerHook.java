/*
 * Created on 7 Mar 2008
 */
package uk.org.ponder.rsf.view.support;

import uk.org.ponder.rsf.processor.HandlerHook;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.view.DataView;
import uk.org.ponder.rsf.view.DataViewHandler;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.stringutil.StringGetter;
import uk.org.ponder.util.RunnableInvoker;

public class DataViewHandlerHook implements HandlerHook {

  private DataViewCollector dataViewCollector;
  private ViewParameters viewParametersProxy;
  private DataViewHandler dataViewHandler;
  private StringGetter requestType;
  private RunnableInvoker requestInvoker;

  public void setRequestInvoker(RunnableInvoker requestInvoker) {
    this.requestInvoker = requestInvoker;
  }

  public void setRequestTypeProxy(StringGetter requestType) {
    this.requestType = requestType;
  }
  
  public void setDataViewCollector(DataViewCollector dataViewCollector) {
    this.dataViewCollector = dataViewCollector;
  }
  
  public void setViewParametersProxy(ViewParameters viewParametersProxy) {
    this.viewParametersProxy = viewParametersProxy;
  }

  public void setDataViewHandler(DataViewHandler dataViewHandler) {
    this.dataViewHandler = dataViewHandler;
  }

  public boolean handle() {
    AnyViewParameters viewparamso = viewParametersProxy.get();
    if (!(viewparamso instanceof ViewParameters)) {
      return false;
    }
    final ViewParameters viewparams = (ViewParameters) viewparamso;
    final DataView view = dataViewCollector.getView(viewparams.viewID);
    if (view != null) {
      if (requestType.get().equals(EarlyRequestParser.RENDER_REQUEST)) {
        requestInvoker.invokeRunnable(new Runnable() {
          public void run() {
            dataViewHandler.handleView(view, viewparams);
          }});
        return true;
      }
    }
    return false;
  }

}
