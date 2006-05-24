/*
 * Created on May 17, 2006
 */
package uk.org.ponder.rsf.renderer.html.decorators;

import java.util.Map;

import uk.org.ponder.htmlutil.HTMLUtil;
import uk.org.ponder.rsf.components.decorators.UIColourDecorator;
import uk.org.ponder.rsf.components.decorators.UIDecorator;
import uk.org.ponder.rsf.renderer.DecoratorRenderer;

public class HTMLColourDecoratorRenderer implements DecoratorRenderer {

  public static final String zeros = "000000";

  public String padLeft(String topad) {
    return zeros.substring(6 - topad.length()) + topad;
  }

  public void modifyAttributes(UIDecorator decoratoro, String tagname,
      Map tomodify) {
    UIColourDecorator decorator = (UIColourDecorator) decoratoro;
    if (decorator.foreground != null) {
      String fgstring = padLeft(Integer.toHexString(decorator.foreground
          .getRGB() & 0xffffff));
      HTMLUtil.appendStyle("color", "#" + fgstring, tomodify);
    }
    if (decorator.background != null) {
      String bgstring = padLeft(Integer.toHexString(decorator.background
          .getRGB() & 0xffffff));
      HTMLUtil.appendStyle("background-color", "#" + bgstring, tomodify);
    }
  }

  public Class getRenderedType() {
    return UIColourDecorator.class;
  }

  public String getContentTypes() {
    return "HTML, HTML-FRAGMENT";
  }

}
