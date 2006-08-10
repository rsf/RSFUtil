/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.uitype.UITypes;
import uk.org.ponder.rsf.viewstate.InternalBaseURLProvider;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;

public class ViewParamsFixer implements ComponentProcessor {
  private ViewStateHandler viewstatehandler;
  private InternalBaseURLProvider ibup;
  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }
  
  public void setInternalBaseURLProvider(InternalBaseURLProvider ibup) {
    this.ibup = ibup;
  }
  
  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIInternalLink) {
      UIInternalLink toprocess = (UIInternalLink) toprocesso;
      // any navigation link is assumed to interrupt flow session, so set
      // IUPS parameters to null.
      if (toprocess.target == null) {
        toprocess.target = new UIOutput();
      }
      if (toprocess.viewparams != null) {
        toprocess.target.setValue(viewstatehandler.getFullURL(toprocess.viewparams));
      }
      else {
        String target = toprocess.target.getValue();
        if (target == null || UITypes.isPlaceholder(target)) {
          throw new IllegalArgumentException("UIInternalLink with fullID " + 
              toprocesso.getFullID() + " discovered with neither ViewParameters nor URL");
        }
        toprocess.target.setValue(ibup.getInternalBaseURL() + target);
      }
    }
  }
}
