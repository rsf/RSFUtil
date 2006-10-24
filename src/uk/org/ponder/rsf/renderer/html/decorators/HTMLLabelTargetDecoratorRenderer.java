/*
 * Created on 24 Oct 2006
 */
package uk.org.ponder.rsf.renderer.html.decorators;

import java.util.Map;

import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.components.decorators.UILabelTargetDecorator;
import uk.org.ponder.rsf.renderer.decorator.DecoratorRenderer;

public class HTMLLabelTargetDecoratorRenderer implements DecoratorRenderer {

  public Class getRenderedType() {
    return UILabelTargetDecorator.class;
  }

  public String getContentTypes() {
    return "HTML, HTML-FRAGMENT";
  }

  public void modifyAttributes(UIDecorator decoratoro, String tagname,
      Map tomodify) {
    UILabelTargetDecorator decorator = (UILabelTargetDecorator) decoratoro;
    if (tagname.equals("label")) {
      tomodify.put("for", decorator.targetFullID);
    }
    else
      throw new IllegalArgumentException(
          "UILabelTargetDecorator peered with unrecognised tag " + tagname
              + " (only <label> is supported for HTML)");
  }

}
