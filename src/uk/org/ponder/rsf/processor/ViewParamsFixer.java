/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.rsf.componentprocessor.ComponentProcessor;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;

public class ViewParamsFixer implements ComponentProcessor {
  private ViewStateHandler viewstatehandler;
  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }
  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIInternalLink) {
      UIInternalLink toprocess = (UIInternalLink) toprocesso;
      toprocess.target = viewstatehandler.getFullURL(toprocess.viewparams);
    }
  }
}
