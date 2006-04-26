/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;

public class ViewParamsFixer implements ComponentProcessor {
  private ViewStateHandler viewstatehandler;
  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }
  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIInternalLink) {
      UIInternalLink toprocess = (UIInternalLink) toprocesso;
      // any navigation link is assumed to interrupt flow session, so set
      // IUPS parameters to null.
      if (toprocess.target == null) {
        toprocess.target = new UIOutput();
      }
      toprocess.target.setValue(viewstatehandler.getFullURL(toprocess.viewparams));
    }
  }
}
