/*
 * Created on 9 Feb 2007
 */
package uk.org.ponder.rsf.renderer.html.decorators;

import java.util.Map;

import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.components.decorators.UIDisabledDecorator;
import uk.org.ponder.rsf.renderer.decorator.DecoratorRenderer;

public class HTMLDisabledDecoratorRenderer implements DecoratorRenderer {
  public Class getRenderedType() {
    return UIDisabledDecorator.class;
  }  

  public void modifyAttributes(UIDecorator decoratoro, String tagname, Map tomodify) {
    UIDisabledDecorator decorator = (UIDisabledDecorator) decoratoro;
    tomodify.put("disabled", decorator.disabled? Boolean.TRUE: Boolean.FALSE);
  }

  public String getContentTypes() {
    return "HTML, HTML-FRAGMENT";
  }

  
}
