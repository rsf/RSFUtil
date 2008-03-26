/*
 * Created on 7 Mar 2008
 */
package uk.org.ponder.rsf.view.support;

import uk.org.ponder.rsf.flow.errors.ViewExceptionStrategy;
import uk.org.ponder.rsf.processor.RedirectingHandlerHook;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.view.DataView;
import uk.org.ponder.rsf.view.DataViewHandler;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.NoViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.stringutil.StringGetter;
import uk.org.ponder.util.RunnableInvoker;
import uk.org.ponder.util.UniversalRuntimeException;

public class DataViewHandlerHook implements RedirectingHandlerHook {

  private DataViewCollector dataViewCollector;
  private ViewParameters viewParametersProxy;
  private DataViewHandler dataViewHandler;
  private StringGetter requestType;
  private RunnableInvoker requestInvoker;
  private ViewExceptionStrategy viewExceptionStrategy;

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

  public void setViewExceptionStrategy(ViewExceptionStrategy viewExceptionStrategy) {
    this.viewExceptionStrategy = viewExceptionStrategy;
  }

  public AnyViewParameters handle() {
    AnyViewParameters viewparamso = viewParametersProxy.get();
    if (!(viewparamso instanceof ViewParameters)) {
      return null;
    }
    final ViewParameters viewparams = (ViewParameters) viewparamso;
    
    if (dataViewCollector.hasView(viewparams.viewID)) {
      try {
        if (requestType.get().equals(EarlyRequestParser.RENDER_REQUEST)) {
          requestInvoker.invokeRunnable(new Runnable() {
            public void run() {
              DataView view = dataViewCollector.getView(viewparams.viewID);
              dataViewHandler.handleView(view, viewparams);
            }
          });
          return NoViewParameters.instance;
        }
      }
      catch (Exception e) {
        AnyViewParameters redirect = viewExceptionStrategy.handleException(e, viewparams);
        if (redirect == null) {
          throw UniversalRuntimeException.accumulate(e, "Unhandled error rendering data view");
        }
        return redirect;
        // ignore any redirect for now
      }
    }
    return null;
  }

}
