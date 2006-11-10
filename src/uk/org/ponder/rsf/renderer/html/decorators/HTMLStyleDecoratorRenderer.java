/*
 * Created on May 17, 2006
 */
package uk.org.ponder.rsf.renderer.html.decorators;

import java.util.Map;

import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.components.decorators.UIStyleDecorator;
import uk.org.ponder.rsf.renderer.decorator.DecoratorRenderer;

public class HTMLStyleDecoratorRenderer implements DecoratorRenderer {

  public Class getRenderedType() {
    return UIStyleDecorator.class;
  }

  public void modifyAttributes(UIDecorator decoratoro, String tagname, Map tomodify) {
    UIStyleDecorator decorator = (UIStyleDecorator) decoratoro;
    String classes = (String) tomodify.get("class");
    if (classes == null) {
      classes = decorator.styleclass;
    }
    else {
      classes = classes + " " + decorator.styleclass;
    }
    tomodify.put("class", classes);
  }

  public String getContentTypes() {
    return "HTML, HTML-FRAGMENT";
  }

}
