/*
 * Created on 26-May-2006
 */
package uk.org.ponder.rsf.renderer.html.decorators;

import java.util.Map;

import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.renderer.decorator.DecoratorRenderer;

public class HTMLTooltipDecoratorRenderer implements DecoratorRenderer {

  public Class getRenderedType() {
    return UITooltipDecorator.class;
  }

  public String getContentTypes() {
    return "HTML, HTML-FRAGMENT";
  }

  public void modifyAttributes(UIDecorator decoratoro, String tagname, Map tomodify) {
    UITooltipDecorator decorator = (UITooltipDecorator) decoratoro;
    tomodify.put("title", decorator.text);
  }

}
