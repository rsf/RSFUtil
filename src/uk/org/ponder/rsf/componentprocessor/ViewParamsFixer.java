/*
 * Created on Oct 25, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import javax.swing.plaf.basic.BasicScrollPaneUI.VSBChangeListener;

import uk.org.ponder.conversion.StaticLeafParser;
import uk.org.ponder.htmlutil.HTMLUtil;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.uitype.UITypes;
import uk.org.ponder.rsf.viewstate.InternalURLRewriter;
import uk.org.ponder.rsf.viewstate.RawViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;

public class ViewParamsFixer implements ComponentProcessor {
  private ViewStateHandler viewstatehandler;
  private InternalURLRewriter inturlrewriter;
  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }
  
  public void setInternalURLRewriter(InternalURLRewriter inturlrewriter) {
    this.inturlrewriter = inturlrewriter;
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
        toprocess.target.setValue(inturlrewriter.rewriteRenderURL(target));
      }
    }
    else if (toprocesso instanceof UIInitBlock) {
      UIInitBlock toprocess = (UIInitBlock) toprocesso;
      String[] rendered = new String[toprocess.arguments.length];
      for (int i = 0; i < toprocess.arguments.length; ++ i) {
        rendered[i] = convertInitArgument(toprocess.arguments[i]);
      }
      toprocess.markup = HTMLUtil.emitJavascriptCall(toprocess.functionname, rendered);
    }
  }

  private String convertInitArgument(Object object) {
    // TODO: upgrade this to an implementation capable of rendering JSON
    StaticLeafParser parser = StaticLeafParser.instance();
    if (parser.isLeafType(object.getClass())) {
      return parser.render(object);
    }
    if (object instanceof UIComponent) {
      return ((UIComponent)object).getFullID();
    }
    if (object instanceof ViewParameters) {
      return viewstatehandler.getFullURL((ViewParameters) object);
    }
    if (object instanceof RawViewParameters) {
      return ((RawViewParameters)object).URL;
    }
    throw new IllegalArgumentException("Cannot render init block argument of unrecognised " + object.getClass());
  }
}
