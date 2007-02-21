/*
 * Created on 20 Feb 2007
 */
package uk.org.ponder.rsf.renderer.html.decorators;

import java.util.Map;

import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.components.decorators.UITextDimensionsDecorator;
import uk.org.ponder.rsf.renderer.decorator.DecoratorRenderer;

public class HTMLTextDimensionsDecoratorRenderer implements DecoratorRenderer {

  public Class getRenderedType() {
    return UITextDimensionsDecorator.class;
  }

  public String getContentTypes() {
    return "HTML, HTML-FRAGMENT";
  }

  public void modifyAttributes(UIDecorator decoratoro, String tagname, Map tomodify) {
    UITextDimensionsDecorator decorator = (UITextDimensionsDecorator) decoratoro;
    if (tagname.equals("textarea")) {
      if (decorator.columns != UITextDimensionsDecorator.UNSET_VALUE) {
        tomodify.put("cols", Integer.toString(decorator.columns));
      }
      if (decorator.rows != UITextDimensionsDecorator.UNSET_VALUE) {
        tomodify.put("rows", Integer.toString(decorator.rows));
      }
    }
    else if (tagname.equals("input")) {
      if (decorator.columns != UITextDimensionsDecorator.UNSET_VALUE) {
        tomodify.put("size", Integer.toString(decorator.columns));
      }
      else if (decorator.rows != UITextDimensionsDecorator.UNSET_VALUE) {
        throw new 
        IllegalArgumentException("Cannot set rows property on tag of type <input>");
      }
    }
    else {
      throw new 
        IllegalArgumentException("Cannot set text dimensions on HTML tag of type " + tagname);
    }
  }

}
