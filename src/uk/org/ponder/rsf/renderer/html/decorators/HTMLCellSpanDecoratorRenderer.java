/*
 * Created on 11 Sep 2007
 */
package uk.org.ponder.rsf.renderer.html.decorators;

import java.util.Map;

import uk.org.ponder.rsf.components.decorators.UICellSpanDecorator;
import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.renderer.decorator.DecoratorRenderer;

public class HTMLCellSpanDecoratorRenderer implements DecoratorRenderer {

  public Class getRenderedType() {
    return UICellSpanDecorator.class;
  }

  public String getContentTypes() {
    return "HTML, HTML-FRAGMENT";
  }

  public void modifyAttributes(UIDecorator decoratoro, String tagname,
      Map tomodify) {
    UICellSpanDecorator decorator = (UICellSpanDecorator) decoratoro;
    if (tagname.equals("td") || tagname.equals("th")) {
      if (decorator.colspan != UICellSpanDecorator.UNSET_VALUE) {
        tomodify.put("colspan", Integer.toString(decorator.colspan));
      }
      if (decorator.rowspan != UICellSpanDecorator.UNSET_VALUE) {
        tomodify.put("rowspan", Integer.toString(decorator.rowspan));
      }
    }
    else {
      throw new IllegalArgumentException(
          "Cannot set cell span on HTML tag of type " + tagname
              + "(only <th> and <td> are supported)");
    }
  }

}
