/*
 * Created on Nov 19, 2006
 */
package uk.org.ponder.rsf.request;

import java.util.Map;

import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsMapper;
import uk.org.ponder.util.RunnableInvoker;

public class LazarusRedirector {
  private ViewParamsMapper viewParamsMapper;
  private RunnableInvoker lazarusListReceiver;
  private RSACBeanLocator rsacbl;

  public void setViewParamsMapper(ViewParamsMapper viewParamsMapper) {
    this.viewParamsMapper = viewParamsMapper;
  }
  
  public void setLazarusListReceiver(RunnableInvoker lazarusListReceiver) {
    this.lazarusListReceiver = lazarusListReceiver;
  }
  
  public void lazarusRedirect(final ViewParameters target) {
    lazarusListReceiver.invokeRunnable(new Runnable() {
      public void run() {
        Map params = viewParamsMapper.renderViewParamAttributes(target);
        String pathinfo = target.toPathInfo();
        
        
        // TODO Auto-generated method stub
        
      }});
  }


  
}
