/*
 * Created on 20 Feb 2007
 */
package uk.org.ponder.rsf.renderer.html.decorators;

import java.util.Map;

import uk.org.ponder.rsf.components.decorators.UIAlternativeTextDecorator;
import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.renderer.decorator.DecoratorRenderer;

public class HTMLAlternativeTextRenderer implements DecoratorRenderer {
  public Class getRenderedType() {
    return UIAlternativeTextDecorator.class;
  }

  public String getContentTypes() {
    return "HTML, HTML-FRAGMENT";
  }

  public void modifyAttributes(UIDecorator decoratoro, String tagname, Map tomodify) {
    UIAlternativeTextDecorator decorator = (UIAlternativeTextDecorator) decoratoro;
    tomodify.put("alt", decorator.text.getValue());
  }

}
