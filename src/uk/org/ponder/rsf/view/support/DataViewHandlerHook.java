/*
 * Created on 7 Mar 2008
 */
package uk.org.ponder.rsf.view.support;

import uk.org.ponder.rsf.processor.HandlerHook;
import uk.org.ponder.rsf.view.DataView;
import uk.org.ponder.rsf.view.DataViewHandler;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class DataViewHandlerHook implements HandlerHook {

  private DataViewCollector dataViewCollector;
  private ViewParameters viewParametersProxy;
  private DataViewHandler dataViewHandler;

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
    ViewParameters viewparams = (ViewParameters) viewparamso;
    DataView view = dataViewCollector.getView(viewparams.viewID);
    if (view != null) {
      dataViewHandler.handleView(view, viewparams);
      return true;
    }
    return false;
  }

}
