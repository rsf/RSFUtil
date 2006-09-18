/*
 * Created on May 17, 2006
 */
package uk.org.ponder.rsf.renderer.decorator;

import java.util.Map;

import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;

public class FreeAttributeDecoratorRenderer implements DecoratorRenderer {

  public Class getRenderedType() {
    return UIFreeAttributeDecorator.class;
  }

  public String getContentTypes() {
    // FADR is equally good (or bad!) for all dialects
    return "";
  }

  public void modifyAttributes(UIDecorator decoratoro, String tagname, Map tomodify) {
    UIFreeAttributeDecorator decorator = (UIFreeAttributeDecorator) decoratoro;
    tomodify.putAll(decorator.attributes);
  }

}
